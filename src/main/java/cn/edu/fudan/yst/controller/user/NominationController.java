package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.dao.NominationDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.Nomination;
import cn.edu.fudan.yst.service.CommonCacheService;
import cn.edu.fudan.yst.storage.FileSystemStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.nio.file.Files;
import java.util.*;

/**
 * 提名的controller
 *
 * @author gzdaijie
 */
@CrossOrigin(methods = { RequestMethod.GET, RequestMethod.POST, RequestMethod.DELETE }, origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/hongtan/vote/api/user/nominations")
public class NominationController {

    /**
     * 个人简介最长15个字
     */
    private final static int INTRO_LENGTH_MAX = 15;
    private final static int STORY_LENGTH_MAX = 140;


    @Autowired
    private NominationDao nominationDao;

    @Autowired
    private CommonCacheService commonCacheService;
    @Autowired
    private FileSystemStorageService storageService;


    /**
     * 根据id 获取某一提名人的信息
     * @param id
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/Nomination/{id}")
    public MyResponse getNominationById(@PathVariable("id") String id,
                                        HttpServletResponse response) throws Exception {
        Nomination nomination = nominationDao.findOne(id);
        if (nomination != null) {
            return new MyResponse(nomination);
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        return null;
    }


    /**
     * 获取所有通过的提名者
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/isPassed")
    public MyResponse getPassedNominations(HttpServletResponse response) throws Exception {
        try {
            return new MyResponse(commonCacheService.get(CommonCacheService.PASSED_NOMINATIONS));
//            List<Nomination> nominations = nominationDao.findByIsPassed(true);
//            return new MyResponse(nominations);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器正忙，稍后再试");
        }
        return new MyResponse(null);
    }

    /**
     * 新增一个提名
     * @param nomination
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    @Transactional
    public MyResponse addNomination(@RequestBody Nomination nomination,
                                    HttpServletResponse response) throws Exception {
        nomination.setId(null);
        nomination.setPassed(false);
        nomination.setCreatedTime(new Date());
        nomination.setLastModifiedTime(new Date());

        String error = hasError(nominationDao, nomination);
        if (error == null) {
            if (nomination.getOriginId() != null) {
                Nomination origin = nominationDao.findOne(nomination.getOriginId());
                if (origin != null) {
                    origin.setDirty(true);
                    nominationDao.save(origin);
                }
            }
            // 新增一个提名
            if(nomination.getRefereeId().length()>0){
                return new MyResponse(nominationDao.save(nomination));
            }
        }
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, error);
        return null;
    }

    /**
     *
     * @param file
     * @param response
     * @return the filename on the server disk
     * @throws Exception
     */
    @PostMapping("/photo")
    @Transactional
    public MyResponse savePhoto(@RequestParam("file") MultipartFile file,
                                    HttpServletResponse response) throws Exception {
        try {
            String filename = storageService.store(file); //TODO: delete the duplicated photo

            return new MyResponse(filename);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "服务器正忙，稍后再试");
        }
        return new MyResponse(null);
    }

    @GetMapping("/photo/{photo}")
    public MyResponse loadPhoto(@PathVariable("photo") String photo,
                                HttpServletResponse response) throws Exception {

        Resource file = storageService.loadAsResource(photo);
        System.out.println(file.getFilename());
        if (file != null) {
            // 清除提名人信息, 再返回前端
            String encodeImage = Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.getFile().toPath()));
            return new MyResponse(encodeImage);
        }
        response.sendError(HttpServletResponse.SC_NOT_FOUND, "Not Found");
        return null;

        //return new MyResponse(nomination);
    }


    public static String hasError(NominationDao nominationDao, Nomination nomination) {
        if (nomination.getOriginId() != null && nomination.getOriginId().isEmpty()) {
            nomination.setOriginId(null);
        }

        if (StringUtils.isEmpty(nomination.getName()) || StringUtils.isEmpty(nomination.getIntroduction())) {
            return "姓名或个人简介不能为空";
        }

        if(nomination.getIntroduction().length() > INTRO_LENGTH_MAX) {
            return "简介不超过15字";
        }

        if (StringUtils.isEmpty(nomination.getReferee()) || StringUtils.isEmpty(nomination.getRefereeContactInfo())) {
            return "正确填写提名/修改人姓名和联系方式";
        }

        if (!StringUtils.isEmpty(nomination.getOriginId()) && !nominationDao.exists(nomination.getOriginId())) {
            return "待修改的被提名人不存在";
        }
        if (StringUtils.isEmpty(nomination.getRefereeId())) {
            return "提名人未认证";
        }
        if (nomination.getStory() !=null  && nomination.getStory().length()>STORY_LENGTH_MAX) {
            return "故事长度不能超过140字";
        }
        return null;
    }
}
