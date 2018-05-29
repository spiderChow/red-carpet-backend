package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/user/vote-rules")
public class VoteRuleController {

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 获得所有的投票规则
     * @return
     * @throws Exception
     */
    @GetMapping("/")
    public MyResponse getAllRules() throws Exception {
        return new MyResponse(commonCacheService.get(CommonCacheService.VOTE_RULES));
    }
}
