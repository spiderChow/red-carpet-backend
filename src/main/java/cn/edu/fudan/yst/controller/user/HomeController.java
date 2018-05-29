package cn.edu.fudan.yst.controller.user;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author gzdaijie
 */
@Controller
@RequestMapping("")
public class HomeController {

    @RequestMapping("")
    public String index() {

        return "index.html";
    }

    @RequestMapping("/oauth")
    public String oauth(HttpServletRequest request) {
        request.setAttribute("key", "hello world");

        return "index.html";
    }

//    //http://example.com/auth_callback?client_id=example&code=d0c08873-6328-43f4-a1a0-2ece098a4aec&scope=username&state=1234
//    @RequestMapping("/oauth") // 还有很多参数怎么处理？
//    public String oauth() throws IOException {
//        String url = "https://tac.fudan.edu.cn/oauth2/authorize.act?client_id=1b135b2c-21ec-40ff-8848-f46233c644a1&response_type=code&state=1234&redirect_uri=http://yst.fudan.edu.cn/oauth";
//
//        URL obj = new URL(url);
//        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
//
//        //默认值我GET
//        con.setRequestMethod("GET");
//
//        //添加请求头
//        con.setRequestProperty("User-Agent", USER_AGENT);
//
//        int responseCode = con.getResponseCode();
//        System.out.println("\nSending 'GET' request to URL : " + url);
//        System.out.println("Response Code : " + responseCode);
//
//        BufferedReader in = new BufferedReader(
//                new InputStreamReader(con.getInputStream()));
//        String inputLine;
//        StringBuffer response = new StringBuffer();
//
//        while ((inputLine = in.readLine()) != null) {
//            response.append(inputLine);
//        }
//        in.close();
//
//        //打印结果
//        System.out.println(response.toString());
//
//        return response.toString();
//    }



}

