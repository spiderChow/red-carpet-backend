package cn.edu.fudan.yst.service;

import cn.edu.fudan.yst.dao.VoteDao;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
 * @author gzdaijie
 */
@Service
public class VoteCacheService {

    @Autowired
    private VoteDao voteDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteCacheService.class);

    public static final String VOTES = "VOTES";


    private LoadingCache<String, Object> cache = CacheBuilder.newBuilder()
            .maximumSize(10).build(new CacheLoader<String, Object>() {
                @Override
                public Object load(String appKey) {
                    LOGGER.info("cache search key VOTES " + new Date());
                    return voteDao.findVoteCount();
                }
            });

    public Object get(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return new Object();
        }
        return cache.get(key);
    }

    public void refresh(String key) throws Exception {
        if (StringUtils.isEmpty(key)) {
            return;
        }
        cache.refresh(key);
    }
}
