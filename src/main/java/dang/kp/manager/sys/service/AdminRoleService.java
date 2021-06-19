package dang.kp.manager.sys.service;

import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.sys.pojo.BaseAdminRole;

import java.util.List;

/**
 * @Title: AdminRoleService
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 14:25
 */
public interface AdminRoleService {

    PageDataResult getRoleList(Integer pageNum, Integer pageSize);

    List<BaseAdminRole> getRoles();

    BaseAdminRole findRoleById(String id);

    Result updateRole(BaseAdminRole role);

    Result delRole(String id, Integer status);

    Result recoverRole(String id, Integer status);

    Result addRole(BaseAdminRole role);

}
