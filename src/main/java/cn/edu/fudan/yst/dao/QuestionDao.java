package cn.edu.fudan.yst.dao;

import cn.edu.fudan.yst.model.db.Question;
import org.springframework.data.repository.CrudRepository;

public interface QuestionDao extends CrudRepository<Question, Long> {

}
