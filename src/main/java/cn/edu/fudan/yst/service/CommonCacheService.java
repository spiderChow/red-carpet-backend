package cn.edu.fudan.yst.service;

import cn.edu.fudan.yst.dao.*;
import cn.edu.fudan.yst.model.db.Nomination;
import cn.edu.fudan.yst.model.db.School;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
 * @author gzdaijie
 */
@Service
public class CommonCacheService {
    private final static long EXPIRE_SECOND = 7200;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommonCacheService.class);

    public static final String SCHOOLS = "SCHOOLS";
    public static final String NOMINEE_TYPES = "NOMINEE_TYPES";
    public static final String VOTE_RULES = "VOTE_RULES";
    public static final String SHARE_CONTENTS = "SHARE_CONTENTS";
    public static final String PASSED_NOMINATIONS = "PASSED_NOMINATIONS";

    @Autowired
    private NominationDao nominationDao;

    @Autowired
    private SchoolDao schoolDao;

    @Autowired
    private NomineeTypeDao nomineeTypeDao;

    @Autowired
    private VoteRuleDao voteRuleDao;

    @Autowired
    private ShareContentDao shareContentDao;

    /**
     * maximumSize == 10
     */
    private LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(10).build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String appKey) {

                    LOGGER.info("cache search key " + appKey + " " + new Date());

                    if (appKey.equals(CommonCacheService.PASSED_NOMINATIONS)) {

                        List<Nomination> nominations = nominationDao.findByIsPassed(true);

                        if (nominations != null) {
                            nominations.forEach(Nomination::clearRefereeInfo);
                        }

                        return nominations;
                    }

                    if (appKey.equals(CommonCacheService.SCHOOLS)) {
                        List<School> schools = schoolDao.findAll();
                        System.out.println(schools.size());
                        return schoolDao.findAll();
                    }

                    if (appKey.equals(CommonCacheService.NOMINEE_TYPES)) {
                        return nomineeTypeDao.findAll();
                    }

                    if (appKey.equals(CommonCacheService.VOTE_RULES)) {
                        return voteRuleDao.findAll();
                    }

                    if (appKey.equals(CommonCacheService.SHARE_CONTENTS)) {
                        return shareContentDao.findAll();
                    }
                    return new Object();
                }
            });

    /**
     * 获取缓存的方法
     * @param key
     * @return
     * @throws Exception
     */
    public Object get(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return new Object();
        }
        return cache.get(key);
    }

    /**
     * 清除缓存的方法
     * @param key
     * @throws Exception
     */
    public void invalidate(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        cache.invalidate(key);
    }
}
