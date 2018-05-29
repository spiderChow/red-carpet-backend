package cn.edu.fudan.yst.controller.user;

import cn.edu.fudan.yst.Utils;
import cn.edu.fudan.yst.dao.VoteDao;
import cn.edu.fudan.yst.model.MyResponse;
import cn.edu.fudan.yst.model.db.Nomination;
import cn.edu.fudan.yst.model.db.Vote;
import cn.edu.fudan.yst.service.VoteCacheService;
import com.mysql.jdbc.StringUtils;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author gzdaijie
 */
@RestController
@RequestMapping("/hongtan/vote/api/user/votes")
public class VoteController {

    @Autowired
    private VoteDao voteDao;

    @Autowired
    private VoteCacheService voteCacheService;

    private final static int VOTE_LIMIT_COUNT = 5;

    /**
     * 查某个被提名人的票数
     * @param id
     * @return
     */
    @GetMapping("/nomination/{id}")
    public MyResponse getByNominationId(@PathVariable("id") String id) {
        return new MyResponse("OK", voteDao.countByNominationId(id));
    }

    /**
     * 查所有人的票数
     * @return
     * @throws Exception
     */
    @GetMapping("/nominations")
    public MyResponse getAllNominations() throws Exception {
        return new MyResponse("OK", voteCacheService.get(VoteCacheService.VOTES));
    }

    /**
     * 完成一次投票
     * @param ids 勾选的所有被提名者的id9
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @PostMapping("/")
    public MyResponse vote(@RequestBody List<String> ids,
                           HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        boolean isValid = true;
        String message = "";
        if (request.getCookies() != null && request.getCookies().length > 0) {
            List<Cookie> cookies = Arrays.asList(request.getCookies());
            for (Cookie cookie: cookies) {
                if ("vote".equals(cookie.getName())) {
                    isValid = false;
                    message = "该设备24小时内已经投过票了~";
                    break;
                }
            }
        }

        if (ids == null || ids.size() > VOTE_LIMIT_COUNT) {
            isValid = false;
            message = "每次可投1-5人";
        }

        // 获取投票人的IP
        String ipAddress = request.getHeader(Utils.REMOTE_ADDR_HEADER);
        if (isValid && StringUtils.isNullOrEmpty(ipAddress)) {
            isValid = false;
            message = "IP地址有误"+ipAddress;
        }
        if (isValid && ipAddress.contains(",")) {
            List<String> ips = StringUtils.split(ipAddress, ",", true);
            ipAddress = ips.get(ips.size() - 1);
        }

        if (isValid && !Utils.isIp(ipAddress)) {
            isValid = false;
            message = "IP地址有误";
        }

        List<Vote> votes = new ArrayList<>();

        int weight = Utils.isInnerIp(ipAddress) ? 8 : 2;  // 校内校外的投票权重对比

        if (isValid) {
            for (String id: ids) {
                Vote vote = new Vote();
                vote.setIpAddress(ipAddress);
                vote.setDate(new Date());
                vote.setDateDetail(new Date());
                vote.setNomination(new Nomination(id));
                vote.setWeight(weight);

                // 根据ip和日期查询, 一个ip一天只能投一次
                if(voteDao.countByIpAddressAndDate(ipAddress, vote.getDate()) > 0) {
                    isValid = false;
                    message = "该IP今天已经投过票了~";
                    break;
                }

                votes.add(vote);
            }
        }

        if (isValid) {
            try {
                voteDao.save(votes);
            } catch (Exception e) {
                isValid = false;
                message = "候选人不存在，请重新选择";
            }
        }

        if (isValid) {
            voteCacheService.refresh(VoteCacheService.VOTES);
            return new MyResponse(String.valueOf(weight));
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
        return null;
    }


    /**
     * 完成一次管理员投票
     * @param
     * @param request
     * @param response
     * @return
     * @throws Exception
     */
    @GetMapping("/super/{id}/{weight}")
    public MyResponse superVote(@PathVariable("id") String id,
                                @PathVariable("weight") String weight,
                           HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        // body {"id":"2f249ead-d124-4a76-8769-b0f62fc7f1b0","weight":"11"}
       // System.out.println(body);

        System.out.println(id);
        System.out.println(weight);
        boolean isValid = true;
        String message = "";


        // 获取投票人的IP
        String ipAddress = request.getHeader(Utils.REMOTE_ADDR_HEADER);
        System.out.println(ipAddress);
        if (isValid && StringUtils.isNullOrEmpty(ipAddress)) {
            isValid = false;
            message = "IP地址有误"+ipAddress;
        }
        if (isValid && ipAddress.contains(",")) {
            List<String> ips = StringUtils.split(ipAddress, ",", true);
            ipAddress = ips.get(ips.size() - 1);
        }

        if (isValid && !Utils.isIp(ipAddress)) {
            isValid = false;
            message = "IP地址有误";
        }

        if (isValid && !Utils.isInnerIp(ipAddress)) {
            isValid = false;
            message = "管理员需连接内网";
        }

        Vote vote = new Vote();
        vote.setIpAddress(ipAddress);
        vote.setDate(new Date());
        vote.setDateDetail(new Date());
        vote.setNomination(new Nomination(id));
        vote.setWeight(Integer.parseInt(weight));



        if (isValid) {
            try {
                voteDao.save(vote);
            } catch (Exception e) {
                isValid = false;
                message = "服务器异常";
            }
        }

        if (isValid) {
            voteCacheService.refresh(VoteCacheService.VOTES);
            return new MyResponse(String.valueOf(weight));
        }

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, message);
        return null;
    }


}


