package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.WeChatUtils;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/user/we-chat")
@PropertySource("classpath:/we-chat.properties")
public class WeChatController {

    @Value("${we-chat.app-id}")
    private String appId;

    @Value("${we-chat.app-secret}")
    private String appSecret;

    @Autowired
    private CommonCacheService commonCacheService;

    @GetMapping("/share-config")
    public MyResponse getShareConfig(@RequestParam("url") String url) throws Exception {
        return new MyResponse(WeChatUtils.getWeChatShareConfig(appId, appSecret, url));
    }

    @GetMapping("/share-contents/")
    public MyResponse getShareContent() throws Exception {
        return new MyResponse(commonCacheService.get(CommonCacheService.SHARE_CONTENTS));
    }
}
