package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.dao.NomineeTypeDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.NomineeType;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/admin/nominee-types")
public class AdminNomineeTypeController {

    @Autowired
    private NomineeTypeDao nomineeTypeDao;

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 新增一个被提名的类型
     * @param nomineeType
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    //@Secured(Role.SUPER_ADMIN)
    public MyResponse postNomineeType(@RequestBody NomineeType nomineeType, HttpServletResponse response) throws Exception {
        try {
            commonCacheService.invalidate(CommonCacheService.NOMINEE_TYPES);
            return new MyResponse(nomineeTypeDao.save(nomineeType));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }

    /**
     * 根据id删除某一个被提名类型
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @DeleteMapping("/nominee-type/{id}")
    //@Secured(Role.SUPER_ADMIN)
    public MyResponse deleteNomineeTypeById(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(id) || !nomineeTypeDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        nomineeTypeDao.delete(id);
        commonCacheService.invalidate(CommonCacheService.NOMINEE_TYPES);
        return new MyResponse(null);
    }
}
