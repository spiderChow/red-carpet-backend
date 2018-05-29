package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.controller.user.NominationController;
import cn.edu.fudan.yst.dao.NominationDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.Role;
import cn.edu.fudan.yst.model.db.Nomination;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author gzdaijie
 */
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.DELETE }, origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hongtan/vote/api/admin/nominations")
public class AdminReviewController {

    @Autowired
    private NominationDao nominationDao;

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 通过某一提名
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    // @Secured(Role.ADMIN)
    @PutMapping("/nomination/{id}")
    @Transactional
    public MyResponse toggleNominationPassedById(@PathVariable("id") String id,
                                                 HttpServletResponse response) throws Exception {
        Nomination nomination = nominationDao.findOne(id);
        if (nomination != null) {
            nomination.setPassed(!nomination.isPassed());
            nomination.setLastModifiedTime(new Date());
            commonCacheService.invalidate(CommonCacheService.PASSED_NOMINATIONS);
            return new MyResponse(nominationDao.save(nomination));
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        return null;
    }

    @Secured(Role.ADMIN)
    @GetMapping("/amendments/{id}")
    public MyResponse getNominationAmends(@PathVariable("id") String id, HttpServletResponse response) {
        Nomination nomination = nominationDao.findOne(id);
        if (nomination != null) {
            Map<String, Object> map = new HashMap<>();
            map.put("nomination", nomination);
            map.put("amendments", nominationDao.findByOriginId(id));
            return new MyResponse(map);
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new MyResponse("Not Found", null);
    }

    // @Secured(Role.ADMIN)
    @GetMapping("/")
    public MyResponse getAllNominations() {
        return new MyResponse(nominationDao.findByOriginIdIsNull());
    }

    /**
     * 更新
     * @param nomination
     * @param response
     * @return
     * @throws Exception
     */
    // @Secured(Role.ADMIN)
    @PutMapping("/")
    @Transactional
    public MyResponse updateNomination(@RequestBody Nomination nomination,
                                       HttpServletResponse response) throws Exception {
        String id = nomination.getId();
        if (StringUtils.isEmpty(id) || !nominationDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }

        String error = NominationController.hasError(nominationDao, nomination);
        if (error == null) {
            nomination.setDirty(false);
            commonCacheService.invalidate(CommonCacheService.PASSED_NOMINATIONS);
            nomination.setLastModifiedTime(new Date());
            return new MyResponse(nominationDao.save(nomination));
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
        return new MyResponse(error, null);
    }

    // @Secured(Role.SUPER_ADMIN)
    @DeleteMapping("/nomination/{id}")
    @Transactional
    public MyResponse deleteNominationById(@PathVariable("id") String id,
                                           HttpServletResponse response) throws Exception {
        Nomination nomination = nominationDao.findOne(id);
        if (nomination != null) {
            nominationDao.delete(nomination);
            nominationDao.deleteByOriginId(id);
            commonCacheService.invalidate(CommonCacheService.PASSED_NOMINATIONS);
            return new MyResponse("删除成功", null);
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new MyResponse("Not Found", null);
    }

    @PostMapping("/refereeinfo")
    public MyResponse verifyRefereeInfo(@RequestBody Nomination nomination,
                                        HttpServletResponse response) throws Exception {
        Nomination realNomination = nominationDao.findOne(nomination.getId());
        if (realNomination != null) {
            if(realNomination.getRefereeContactInfo().equals(nomination.getRefereeContactInfo())){
                if(realNomination.getReferee().equals(nomination.getReferee())){
                    return new MyResponse("ok", true);

                }
            }
            return new MyResponse("ok", false);
        }
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        return new MyResponse("Not Found", false);
    }

}
