package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.dao.ShareContentDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.Role;
import cn.edu.fudan.yst.model.db.ShareContent;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/admin/we-chat")
public class AdminWeChatController {
    @Autowired
    private ShareContentDao shareContentDao;

    @Autowired
    private CommonCacheService commonCacheService;

    @PostMapping("/share-contents/")
    @Secured(Role.SUPER_ADMIN)
    public MyResponse postShareContent(@RequestBody ShareContent shareContent,
                                       HttpServletResponse response) throws Exception {
        try {
            commonCacheService.invalidate(CommonCacheService.SHARE_CONTENTS);
            return new MyResponse(shareContentDao.save(shareContent));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }

    @DeleteMapping("/share-contents/share-content/{id}")
    @Secured(Role.SUPER_ADMIN)
    public MyResponse deleteShareContentById(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(id) || !shareContentDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        shareContentDao.delete(id);
        commonCacheService.invalidate(CommonCacheService.SHARE_CONTENTS);
        return new MyResponse(null);
    }
}
