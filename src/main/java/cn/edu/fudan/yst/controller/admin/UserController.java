package cn.edu.fudan.yst.controller.admin;

import cn.edu.fudan.yst.Utils;
import cn.edu.fudan.yst.dao.UserDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author gzdaijie
 */
@RequestMapping("/hongtan/vote/api/admin/users")
@RestController
public class UserController {

    private final static int PASSWORD_LENGTH_LIMIT = 6;

    @Autowired
    private UserDao userDao;

    /**
     * 查询出所有用户
     * @return
     */
    @GetMapping("/")
    // @Secured(Role.ADMIN)
    public MyResponse getUsers() {
        List<User> users = userDao.findAll();
        users.forEach(User::clearPassword);
        return new MyResponse(users);
    }


    @PostMapping("/user/")
    // @Secured(Role.ADMIN)
    public MyResponse UserExisted(@RequestBody User user,
                                  HttpServletResponse response) {
        System.out.println(user.getId());
        boolean isExisted = userDao.exists(user.getId());
        return new MyResponse(isExisted);
    }


    /**
     * 新增一个用户
     * @param user
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse postUser(@RequestBody User user,
                               HttpServletResponse response) throws Exception {
        if (StringUtils.isEmpty(user.getId()) || StringUtils.isEmpty(user.getPassword()) || StringUtils.isEmpty(user.getRoles())) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "必填项不能为空");
            return null;
        }
        if (user.getPassword().length() < PASSWORD_LENGTH_LIMIT) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "密码不低于6位");
            return null;
        }
        user.setPassword(Utils._3md5(user.getPassword()));
        return new MyResponse(userDao.save(user));
    }

    /**
     * 删除一个普通用户
     * @param id
     * @param response
     * @return
     */
    @DeleteMapping("/user/{id}")
    // @Secured(Role.SUPER_ADMIN)
    public MyResponse deleteUserById(@PathVariable("id") String id,
                                     HttpServletResponse response) {

        if (StringUtils.isEmpty(id) || !userDao.exists(id)) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return new MyResponse("Not Found", null);
        }
        userDao.delete(id);
        return new MyResponse(null);
    }
}
