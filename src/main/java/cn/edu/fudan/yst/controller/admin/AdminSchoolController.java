package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.dao.SchoolDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.School;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author gzdaijie
 */
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST,RequestMethod.PUT, RequestMethod.DELETE }, origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hongtan/vote/api/admin/schools")
public class AdminSchoolController {

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private CommonCacheService commonCacheService;

    @PostMapping("/")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse postSchool(@RequestBody School school, HttpServletResponse response) throws Exception {
        try {
            commonCacheService.invalidate(CommonCacheService.SCHOOLS);
            return new MyResponse(schoolDao.save(school));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }

    @PostMapping("/schools/")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse postSchools(@RequestBody List<School> schools, HttpServletResponse response) throws Exception {
        try {
            commonCacheService.invalidate(CommonCacheService.SCHOOLS);
            return new MyResponse(schoolDao.save(schools));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }

    @DeleteMapping("/school/{id}")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse deleteSchoolById(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(id) || !schoolDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        schoolDao.delete(id);
        commonCacheService.invalidate(CommonCacheService.SCHOOLS);
        return new MyResponse(null);
    }
}
