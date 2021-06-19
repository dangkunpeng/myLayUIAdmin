package dang.kp.manager.sys.service;

import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.sys.pojo.BaseAdminPermission;
import dang.kp.manager.sys.pojo.BaseAdminUser;

import java.util.List;

/**
 * @Title: PermissionService
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/30 9:44
 */
public interface AdminPermissionService {

    /**
     * 功能描述: 添加权限
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 9:48
     */
    Result addPermission(BaseAdminPermission permission);

    /**
     * 功能描述: 修改权限
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 9:48
     */
    Result updatePermission(BaseAdminPermission permission);

    /**
     * 功能描述: 获取权限菜单列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 11:35
     */
    PageDataResult getPermissionList(Integer pageNum, Integer pageSize);

    /**
     * 功能描述: 获取根权限菜单列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 11:36
     */
    List<BaseAdminPermission> parentPermissionList();

    /**
     * 功能描述: 删除权限菜单
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/30 12:04
     */
    Result del(String id);

    /**
     * 功能描述: 根据id获取权限
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/12/4 13:51
     */
    BaseAdminPermission getById(String id);


    /**
     * 功能描述: 获取当前登陆用户的权限
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/12/4 13:51
     */
    Result getUserPerms(BaseAdminUser user);

}
