package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

/**
 * @author gzdaijie
 */
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE }, origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hongtan/vote/api/user/schools")
public class SchoolController {

    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 获得所有提名的院系
     * @return
     * @throws Exception
     */
    @GetMapping("/")
    public MyResponse getAll(HttpServletResponse response) throws Exception {
//        return new MyResponse("OK", commonCacheService.get(CommonCacheService.SCHOOLS));
        try {
            return new MyResponse(commonCacheService.get(CommonCacheService.SCHOOLS));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器正忙，稍后再试");
        }
        return new MyResponse(null);
    }
}
