package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.dao.VoteRuleDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.VoteRule;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/admin/vote-rules")
public class AdminVoteRuleController {

    @Autowired
    private VoteRuleDao voteRuleDao;

    @Autowired
    private CommonCacheService commonCacheService;

    @PostMapping("/")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse postVoteRule(@RequestBody VoteRule voteRule, HttpServletResponse response) throws Exception {
        try {
            commonCacheService.invalidate(CommonCacheService.VOTE_RULES);
            return new MyResponse(voteRuleDao.save(voteRule));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }

    @DeleteMapping("/vote-rule/{id}")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse deleteVoteRuleById(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(id) || !voteRuleDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        voteRuleDao.delete(id);
        commonCacheService.invalidate(CommonCacheService.VOTE_RULES);
        return new MyResponse(null);
    }
}
