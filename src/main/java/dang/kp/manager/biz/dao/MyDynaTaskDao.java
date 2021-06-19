package dang.kp.manager.biz.dao;

import dang.kp.manager.biz.pojo.MyDynaTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyDynaTaskDao extends JpaRepository<MyDynaTask, String> {
}
