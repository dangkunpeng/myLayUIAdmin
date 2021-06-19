package dang.kp.manager.sys.dao;


import dang.kp.manager.sys.pojo.BaseAdminPermission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BaseAdminPermissionDao extends JpaRepository<BaseAdminPermission, String> {

    List<BaseAdminPermission> findByPid(String pid);
}