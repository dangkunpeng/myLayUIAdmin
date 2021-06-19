package dang.kp.manager.sys.controller;

import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.sys.pojo.BaseAdminRole;
import dang.kp.manager.sys.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Title: RoleController
 * @Description: 角色管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 13:43
 */
@Slf4j
@Controller
@RequestMapping("role")
public class RoleController {

    @Autowired
    private AdminRoleService adminRoleService;

    /**
     * 跳转到角色管理
     *
     * @return
     */
    @RequestMapping("/roleManage")
    public String toPage() {
        log.info("进入角色管理");

        return "/sys/role/roleManage";
    }

    /**
     * 功能描述: 获取角色列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 14:29
     */
    @RequestMapping(value = "/getRoleList", method = RequestMethod.GET)
    @ResponseBody
    public PageDataResult getRoleList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize) {
        log.info("获取角色列表");
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取角色列表
            pdr = adminRoleService.getRoleList(pageNum, pageSize);
            log.info("角色列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("角色列表查询异常！", e);
        }
        return pdr;
    }

    /**
     * 功能描述: 获取角色列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/12/3 13:22
     */
    @GetMapping("getRoles")
    @ResponseBody
    public List<BaseAdminRole> getRoles() {
        log.info("获取角色列表");
        return adminRoleService.getRoles();
    }

    /**
     * 述: 设置角色[新增或更新]
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/12/3 10:54
     */
    @PostMapping("setRole")
    @ResponseBody
    public Result setRole(BaseAdminRole role) {
        log.info("设置角色[新增或更新]！role:" + role);
        if (role.getId() == null) {
            //新增角色
            return adminRoleService.addRole(role);
        } else {
            //修改角色
            return adminRoleService.updateRole(role);
        }
    }


    /**
     * 功能描述: 删除/恢复角色
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 16:00
     */
    @PostMapping("updateRoleStatus")
    @ResponseBody
    public Result updateRoleStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        log.info("删除/恢复角色！id:" + id + " status:" + status);
        if (status == 0) {
            //删除角色
            return adminRoleService.delRole(id, status);
        } else {
            //恢复角色
            return adminRoleService.recoverRole(id, status);
        }
    }

}
