package dang.kp.manager.sys.dao;

import dang.kp.manager.sys.pojo.DictType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DictTypeDao extends JpaRepository<DictType, String> {
}
