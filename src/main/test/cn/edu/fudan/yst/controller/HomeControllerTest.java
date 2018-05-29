package cn.edu.fudan.yst.controller;

import cn.edu.fudan.yst.Utils;
import cn.edu.fudan.yst.model.db.Vote;
import com.mysql.jdbc.StringUtils;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.StringJoiner;

/**
 * @author gzdaijie
 */
public class HomeControllerTest {
    @Test
    public void testMd5() {
        String a = "admin";
        System.out.println(md5(md5(md5(a))));
        System.out.println(md5(a));
    }



    @Test
    public void testGrantToString() {
        List<GrantedAuthority> authority = AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_ADMIN,ROLE_SUPER_ADMIN");
        StringJoiner joiner = new StringJoiner(",");
        authority.forEach(item -> {
            joiner.add(item.getAuthority());
        });
        System.out.println(joiner.toString());

    }

    @Test
    public void testInnerIp() {
        Assert.assertTrue(Utils.isInnerIp("10.0.0.0"));
        Assert.assertTrue(Utils.isInnerIp("10.255.255.255"));
        Assert.assertTrue(Utils.isInnerIp("172.16.0.0"));
        Assert.assertTrue(Utils.isInnerIp("172.31.255.255"));
        Assert.assertTrue(Utils.isInnerIp("192.168.0.0"));
        Assert.assertTrue(Utils.isInnerIp("192.168.255.255"));
        Assert.assertTrue(Utils.isInnerIp("10.220.196.12"));

        Assert.assertTrue(Utils.isInnerIp("202.120.222.225"));
        Assert.assertTrue(Utils.isInnerIp("218.193.138.2"));

        Assert.assertFalse(Utils.isInnerIp("127.0.0.1"));
        Assert.assertFalse(Utils.isInnerIp("192.167.0.1"));
        Assert.assertFalse(Utils.isInnerIp("172.32.0.1"));
        Assert.assertFalse(Utils.isInnerIp("172.32.245.254"));
        Assert.assertFalse(Utils.isInnerIp("202.120.8.254"));

    }

    @Test
    public void testIsIp() {
        Assert.assertTrue(Utils.isIp("10.0.0.0"));
        Assert.assertTrue(Utils.isIp("10.255.255.255"));
        Assert.assertTrue(Utils.isIp("172.16.0.0"));
        Assert.assertTrue(Utils.isIp("172.31.255.255"));
        Assert.assertTrue(Utils.isIp("192.168.0.0"));
        Assert.assertTrue(Utils.isIp("192.168.255.255"));
        Assert.assertTrue(Utils.isIp("10.220.196.12"));

        Assert.assertTrue(Utils.isIp("202.120.222.225"));
        Assert.assertTrue(Utils.isIp("218.193.138.2"));

        Assert.assertTrue(Utils.isIp("127.0.0.1"));
        Assert.assertTrue(Utils.isIp("192.167.0.1"));
        Assert.assertTrue(Utils.isIp("172.32.0.1"));
        Assert.assertTrue(Utils.isIp("172.32.245.254"));
        Assert.assertTrue(Utils.isIp("202.120.8.254"));
        Assert.assertFalse(Utils.isIp("202.120.8.257"));

    }

    @Test
    public void testSplit() {
        String ipAddress = "10.19.232.43, 10.10.10.10, 10.1.1.2";
        boolean isIp = true;
        if (ipAddress.contains(",")) {
            List<String> ips = StringUtils.split(ipAddress, ",", true);
            ipAddress = ips.get(ips.size() - 1);
            if (!Utils.isIp(ipAddress)) {
                isIp = false;
            }
        }
        System.out.println(ipAddress);
        System.out.println(isIp);
    }

    @Test
    public void testDateFormat() {
        Vote vote = new Vote();
        vote.setDateDetail(new Date());
        System.out.println(vote.getDateDetail());
    }

    @Test
    public void weChatShare() {
        String a = "auhfu_dehfrufr";
        String[] strings = a.split("_");
        System.out.println(strings[0]);
        System.out.println(strings[1]);
    }
    private String md5(String s) {
        return DigestUtils.md5DigestAsHex(s.getBytes());
    }
}
