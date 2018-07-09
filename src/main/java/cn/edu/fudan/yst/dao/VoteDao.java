package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.Vote;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author gzdaijie
 */
public interface VoteDao extends CrudRepository<Vote, Long> {

    /**
     * 根据被提名者的id, 确定总票数
     * @param nominationId
     * @return
     */
    long countByNominationId(String nominationId);

    /**
     * 根据ip和日期, 确定票数
     * @param ipAddress
     * @param date
     * @return
     */
    long countByIpAddressAndDate(String ipAddress, String date);

    /**
     * 查出每个被提名人的票数
     * @return
     */
    @Query(value = "SELECT nomination_id, SUM(weight) FROM votes GROUP BY nomination_id", nativeQuery = true)
    List<?> findVoteCount();

    @Override
    <S extends Vote> List<S> save(Iterable<S> iterable);

p
}
