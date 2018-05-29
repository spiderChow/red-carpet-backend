package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.School;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author gzdaijie
 */
public interface SchoolDao extends CrudRepository<School, Long> {
    @Override
    List<School> findAll();


   // <S extends School> save(Iterable<S> iterable);
}
