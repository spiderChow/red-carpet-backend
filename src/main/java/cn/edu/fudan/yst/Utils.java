package cn.edu.fudan.yst;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author gzdaijie
 *         059.078.082.000 059.078.083.255
 *         061.129.042.000 061.129.042.255
 *         202.038.000.000 202.038.001.255
 *         202.120.064.000 202.120.079.255
 *         202.120.224.000 202.120.255.255
 *         202.122.000.000 202.122.007.255
 *         218.193.128.000 218.193.143.255
 */
public class Utils {
    private static final List<String> FD_IP_LIST = Arrays.asList(
            "59.78.82.",
            "59.78.83.",
            "61.129.42.",
            "202.38.1.",
            "202.120.64.",
            "202.120.65.",
            "202.120.66.",
            "202.120.67.",
            "202.120.68.",
            "202.120.69.",
            "202.120.7",
            "218.193.128.",
            "218.193.129.",
            "218.193.140.",
            "218.193.141.",
            "218.193.142.",
            "218.193.143."
    );

    private static final Pattern pattern0 = Pattern.compile("((192\\.168|172\\.([1][6-9]|[2]\\d|3[01]))"
            + "(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){2}|"
            + "^(\\D)*10(\\.([2][0-4]\\d|[2][5][0-5]|[01]?\\d?\\d)){3})");
    private static final Pattern pattern1 = Pattern.compile("^202.120.2[2-5][0-9].");
    private static final Pattern pattern2 = Pattern.compile("^202.122.[0-7].");
    private static final Pattern pattern3 = Pattern.compile("^218.193.13[0-9].");
    private static final Pattern ipPattern = Pattern.compile("^([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

    private static String md5(String s) {
        if (s != null && !s.isEmpty()) {
            return DigestUtils.md5DigestAsHex(s.getBytes());
        }
        return "";
    }

    /**
     * 判断是否是内网IP
     * @param ip
     * @return
     */
    public static boolean isInnerIp(String ip) {
        if (ip == null || StringUtils.isEmpty(ip)) {
            return false;
        }

        if (pattern0.matcher(ip).find()) {
            return true;
        }

        for (String str : FD_IP_LIST) {
            if (ip.startsWith(str)) {
                return true;
            }
        }

        return pattern1.matcher(ip).find() || pattern2.matcher(ip).find() || pattern3.matcher(ip).find();
    }

    /**
     * 是否是有效的IP
     *
     * @param ip the ip
     * @return the boolean
     */
    public static boolean isIp(String ip) {
        if (StringUtils.isEmpty(ip) || ip.length() < 7 || ip.length() > 15) {
            return false;
        }
        return ipPattern.matcher(ip).find();
    }

    /**
     * 3次md5加密
     *
     * @param s the s
     * @return the string
     */
    public static String _3md5(String s) {
        return md5(md5(md5(s)));
    }

    /**
     * 获取来自代理的IP
     */
    public static final String REMOTE_ADDR_HEADER = "X-Forwarded-For";

    public static void main(String[] args) {
        String a = "admin";
        System.out.println(md5(md5(md5(a))));
        System.out.println(md5(a));
    }
}
