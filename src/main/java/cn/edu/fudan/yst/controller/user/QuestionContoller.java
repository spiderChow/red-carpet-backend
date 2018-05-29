package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.dao.QuestionDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.Question;
import cn.edu.fudan.yst.service.CommonCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

@CrossOrigin(methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE}, origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hongtan/vote/api/admin/questions")
public class QuestionContoller {

    @Autowired
    private QuestionDao questionDao;
    @Autowired
    private CommonCacheService commonCacheService;

    /**
     * 根据id 获取某一随机Question的信息
     *
     * @param response
     * @return
     * @throws Exception
     */

    // TODO: need to test
    @GetMapping("/question/")
    public MyResponse getQuestionById(HttpServletResponse response) throws Exception {
        //get random number
        long leftLimit = 0L;
        long rightLimit = questionDao.count();
        long id = leftLimit + (long) (Math.random() * (rightLimit - leftLimit));


        Question question = questionDao.findOne(id);
        if (question != null) {
            return new MyResponse(question);
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        return null;
    }

    /**
     * 根据id 获取某一Question的信息
     *
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/question/{id}")
    public MyResponse getQuestionById(@PathVariable("id") Long id,
                                        HttpServletResponse response) throws Exception {
        Question question = questionDao.findOne(id);
        if (question != null) {
            return new MyResponse(question);
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        return null;
    }

    @PostMapping("/")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse postQuestion(@RequestBody Question question, HttpServletResponse response) throws Exception {
        try {
            return new MyResponse(questionDao.save(question));
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
            return new MyResponse(null);
        }
    }


    @DeleteMapping("/question/{id}")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse deleteQuestionById(@PathVariable("id") Long id, HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(id) || !questionDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        questionDao.delete(id);
        return new MyResponse(null);
    }
}
