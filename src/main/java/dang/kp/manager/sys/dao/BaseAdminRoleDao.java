package dang.kp.manager.sys.dao;

import dang.kp.manager.sys.pojo.BaseAdminRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseAdminRoleDao extends JpaRepository<BaseAdminRole, String> {

    List<BaseAdminRole> findByRoleStatus(Integer roleStatus);
}