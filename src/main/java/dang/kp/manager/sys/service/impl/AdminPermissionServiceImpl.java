package dang.kp.manager.sys.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.common.result.ResultUtils;
import dang.kp.manager.common.utils.BatchUtils;
import dang.kp.manager.common.utils.DateUtils;
import dang.kp.manager.common.utils.MyConstants;
import dang.kp.manager.sys.dao.BaseAdminPermissionDao;
import dang.kp.manager.sys.dao.BaseAdminRoleDao;
import dang.kp.manager.sys.dto.PermissionDTO;
import dang.kp.manager.sys.pojo.BaseAdminPermission;
import dang.kp.manager.sys.pojo.BaseAdminRole;
import dang.kp.manager.sys.pojo.BaseAdminUser;
import dang.kp.manager.sys.service.AdminPermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @Title: PermissionServiceImpl
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/30 9:44
 */
@Slf4j
@Service
public class AdminPermissionServiceImpl implements AdminPermissionService {

    @Autowired
    private BaseAdminPermissionDao baseAdminPermissionDao;

    @Autowired
    private BaseAdminRoleDao baseAdminRoleDao;

    @Override
    public Result addPermission(BaseAdminPermission permission) {
        try {
            permission.setCreateTime(DateUtils.getCurrentDate());
            permission.setUpdateTime(DateUtils.getCurrentDate());
            permission.setDelFlag(1);
            permission.setId(BatchUtils.getKey(MyConstants.MyKey.BaseAdminPermission));
            this.baseAdminPermissionDao.save(permission);
            log.info("权限[新增]，结果=新增成功！");
            return ResultUtils.success("新增成功！");
        } catch (Exception e) {
            log.error("权限[新增]异常！{}", e);
            return ResultUtils.fail("权限[新增]异常！");
        }
    }

    @Override
    public Result updatePermission(BaseAdminPermission permission) {
        try {
            BaseAdminPermission old = this.baseAdminPermissionDao.getOne(permission.getId());
            BeanUtils.copyProperties(permission, old, MyConstants.SYSTEM_FIELDS);
            old.setUpdateTime(DateUtils.getCurrentDate());
            this.baseAdminPermissionDao.save(old);
            log.info("权限[更新]，结果=更新成功！");
            return ResultUtils.success("更新成功！");
        } catch (Exception e) {
            log.error("权限[更新]异常！{}", e);
            return ResultUtils.fail("权限[更新]异常！");
        }
    }


    @Override
    public PageDataResult getPermissionList(Integer pageNum, Integer pageSize) {
        Sort sort = Sort.by(Sort.Order.desc("pid"));
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<BaseAdminPermission> pageResult = this.baseAdminPermissionDao.findAll(pageable);

        return PageDataResult.builder().list(pageResult.getContent()).totals(pageResult.getTotalPages()).code(200).build();
    }

    @Override
    public List<BaseAdminPermission> parentPermissionList() {
        List<PermissionDTO> permissionList = Lists.newArrayList();
        List<BaseAdminPermission> permList = this.baseAdminPermissionDao.findByPid("0");
        for (BaseAdminPermission perm : permList) {
            // 授权角色下所有权限
            PermissionDTO permissionDTO = new PermissionDTO();
            BeanUtils.copyProperties(perm, permissionDTO);
            permissionList.add(permissionDTO);
        }
        return this.baseAdminPermissionDao.findByPid("0");
    }

    @Override
    public Result del(String id) {
        try {
            // 删除权限菜单
            this.baseAdminPermissionDao.deleteById(id);
            log.info("删除成功");
            return ResultUtils.success("删除成功！");
        } catch (Exception e) {
            log.error("删除权限菜单异常！{}", e);
            return ResultUtils.fail("删除权限菜单异常！");
        }
    }


    @Override
    public BaseAdminPermission getById(String id) {
        return this.baseAdminPermissionDao.findById(id).get();
    }


    @Override
    public Result getUserPerms(BaseAdminUser user) {
        List<PermissionDTO> permissionList = Lists.newArrayList();

        Optional<BaseAdminRole> baseAdminRole = this.baseAdminRoleDao.findById(user.getRoleId());
        if (baseAdminRole.isPresent()) {
            BaseAdminRole role = baseAdminRole.get();
            String permissions = role.getPermissions();

            List<String> permissionIds = Splitter.on(",").splitToList(permissions);
            List<BaseAdminPermission> permList = this.baseAdminPermissionDao.findAllById(permissionIds);
            for (BaseAdminPermission perm : permList) {
                // 授权角色下所有权限
                PermissionDTO permissionDTO = new PermissionDTO();
                BeanUtils.copyProperties(perm, permissionDTO);
                //获取子权限
                List<BaseAdminPermission> childrens = baseAdminPermissionDao.findByPid(perm.getId());
                List<PermissionDTO> items = Lists.newArrayList();
                for (BaseAdminPermission children : childrens) {
                    PermissionDTO item = new PermissionDTO();
                    BeanUtils.copyProperties(children, item);
                    items.add(item);
                }
                permissionDTO.setChildrens(items);
                permissionList.add(permissionDTO);
            }
        }
        return ResultUtils.success(permissionList);
    }
}
