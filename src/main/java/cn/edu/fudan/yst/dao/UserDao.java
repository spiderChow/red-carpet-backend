package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author gzdaijie
 */
public interface UserDao extends CrudRepository<User, String> {
    @Override
    List<User> findAll();

    @Override
    boolean exists(String id);

}
