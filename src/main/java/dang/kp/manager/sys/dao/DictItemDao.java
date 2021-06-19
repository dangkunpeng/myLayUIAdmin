package dang.kp.manager.sys.dao;

import dang.kp.manager.sys.pojo.DictItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DictItemDao extends JpaRepository<DictItem, String> {

    List<DictItem> getByTypeId(String typeId);
}
