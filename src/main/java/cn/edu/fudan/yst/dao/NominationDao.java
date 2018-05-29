package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.Nomination;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

/**
 * @author gzdaijie
 */
public interface NominationDao extends CrudRepository<Nomination, String> {

    ArrayList<Nomination> findByIsPassed(boolean isPassed);

    ArrayList<Nomination> findByOriginId(String originId);

    ArrayList<Nomination> findByOriginIdIsNull();

    void deleteByOriginId(String originId);
}