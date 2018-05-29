package cn.edu.fudan.yst;

import cn.edu.fudan.yst.model.WeChatShare;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONValue;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.util.Formatter;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.springframework.http.HttpHeaders.USER_AGENT;

/**
 * @author gzdaijie
 */
public class WeChatUtils {

    private static final long EXPIRE_SECOND = 7000;
    private String appId = "";
    private String appSecret = "";

    private static LoadingCache<String, String> cache = CacheBuilder.newBuilder()
            .expireAfterWrite(EXPIRE_SECOND, TimeUnit.SECONDS)
            .maximumSize(1000).build(new CacheLoader<String, String>() {
                @Override
                public String load(String appKey) {
                    String[] strings = appKey.split("_");
                    return getTicket(strings[0], strings[1]);
                }
            });

    /**
     * 利用appId和appSecret获取accessToken
     * @param appId
     * @param appSecret
     * @return
     */
    private static String getAccessToken(String appId, String appSecret) {
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=" + appId + "&secret=" + appSecret;
        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);

            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);

            JSONObject object = (JSONObject) JSONValue.parse(inputStreamToString(response.getEntity().getContent()));
            return object.getAsString("access_token");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 利用accessToken获取ticket
     * @param appId
     * @param appSecret
     * @return
     */
    private static String getTicket(String appId, String appSecret) {
        String accessToken = getAccessToken(appId, appSecret);
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=" + accessToken + "&type=jsapi";

        try {
            HttpClient client = HttpClientBuilder.create().build();
            HttpGet request = new HttpGet(url);
            request.addHeader("User-Agent", USER_AGENT);
            HttpResponse response = client.execute(request);
            JSONObject object = (JSONObject) JSONValue.parse(inputStreamToString(response.getEntity().getContent()));
            return object.getAsString("ticket");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static String inputStreamToString(InputStream in) throws Exception {
        BufferedReader rd = new BufferedReader(new InputStreamReader(in));

        StringBuffer result = new StringBuffer();
        String line = "";
        while ((line = rd.readLine()) != null) {
            result.append(line);
        }
        return result.toString();
    }

    public static WeChatShare getWeChatShareConfig(String appId, String appSecret, String url) throws Exception {
        String apiTicket = cache.get(appId + "_" + appSecret);
        String timeStamp = Long.toString(System.currentTimeMillis() / 1000);
        String nonceStr = UUID.randomUUID().toString();

        WeChatShare weChatShare = new WeChatShare();
        weChatShare.setAppId(appId);
        weChatShare.setTimestamp(timeStamp);
        weChatShare.setNonceStr(nonceStr);

        String str = "jsapi_ticket=" + apiTicket + "&noncestr=" + nonceStr + "&timestamp=" + timeStamp + "&url=" + url;

        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(str.getBytes("UTF-8"));
            weChatShare.setSignature(byteToHex(crypt.digest()));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return weChatShare;
    }

    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }
}
