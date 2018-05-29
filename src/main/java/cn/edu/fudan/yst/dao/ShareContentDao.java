package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.ShareContent;
import cn.edu.fudan.yst.model.db.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/**
 * @author gzdaijie
 */
public interface ShareContentDao extends CrudRepository<ShareContent, Long> {
    @Override
    List<ShareContent> findAll();
}
