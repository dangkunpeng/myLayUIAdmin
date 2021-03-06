package dang.kp.manager.sys.service.impl;

import com.google.common.base.Splitter;
import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.common.result.ResultUtils;
import dang.kp.manager.common.utils.BatchUtils;
import dang.kp.manager.common.utils.DateUtils;
import dang.kp.manager.common.utils.MyConstants;
import dang.kp.manager.sys.dao.BaseAdminPermissionDao;
import dang.kp.manager.sys.dao.BaseAdminRoleDao;
import dang.kp.manager.sys.dto.AdminRoleDTO;
import dang.kp.manager.sys.pojo.BaseAdminPermission;
import dang.kp.manager.sys.pojo.BaseAdminRole;
import dang.kp.manager.sys.service.AdminRoleService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Title: AdminRoleServiceImpl
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 14:26
 */
@Slf4j
@Service
public class AdminRoleServiceImpl implements AdminRoleService {

    @Autowired
    private BaseAdminRoleDao baseAdminRoleDao;

    @Autowired
    private BaseAdminPermissionDao baseAdminPermissionDao;

    @Override
    public PageDataResult getRoleList(Integer pageNum, Integer pageSize) {
        PageDataResult pageDataResult = new PageDataResult();
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<BaseAdminRole> pageResult = this.baseAdminRoleDao.findAll(pageable);

        List<AdminRoleDTO> roleList = new ArrayList<>();
        for (BaseAdminRole r : pageResult) {
            AdminRoleDTO roleDTO = new AdminRoleDTO();

            String permissions = r.getPermissions();
            BeanUtils.copyProperties(r, roleDTO);
            roleDTO.setPermissionIds(permissions);

            if (StringUtils.isNotBlank(permissions)) {
                List<String> permissionIds = Splitter.on(",").splitToList(permissions);
                List<BaseAdminPermission> permList = this.baseAdminPermissionDao.findAllById(permissionIds);
                List<String> permName = permList.stream().map(BaseAdminPermission::getName).collect(Collectors.toList());
                roleDTO.setPermissions(permName.toString());
            }
            roleList.add(roleDTO);
        }
        return PageDataResult.builder().list(roleList).totals(pageResult.getTotalPages()).code(200).build();
    }

    @Override
    public Result addRole(BaseAdminRole role) {
        try {
            role.setCreateTime(DateUtils.getCurrentDate());
            role.setUpdateTime(DateUtils.getCurrentDate());
            role.setRoleStatus(1);
            role.setId(BatchUtils.getKey(MyConstants.MyKey.BaseAdminRole));
            this.baseAdminRoleDao.save(role);
            return ResultUtils.success("?????????????????????");
        } catch (Exception e) {
            log.error("?????????????????????{}", e);
            return ResultUtils.fail("?????????????????????");
        }
    }

    @Override
    public BaseAdminRole findRoleById(String id) {
        return this.baseAdminRoleDao.findById(id).get();
    }

    @Override
    public Result updateRole(BaseAdminRole role) {
        try {
            role.setUpdateTime(DateUtils.getCurrentDate());
            this.baseAdminRoleDao.save(role);
            log.info("??????[??????]?????????=???????????????");
            return ResultUtils.success("???????????????");
        } catch (Exception e) {
            log.error("??????[??????]?????????{}", e);
            return ResultUtils.fail("?????????????????????");
        }
    }

    @Override
    public Result delRole(String id, Integer status) {
        try {
            BaseAdminRole role = findRoleById(id);
            role.setRoleStatus(status);
            this.baseAdminRoleDao.save(role);
            return ResultUtils.success("?????????????????????");
        } catch (Exception e) {
            log.error("?????????????????????{}", e);
            return ResultUtils.fail("?????????????????????");
        }
    }

    @Override
    public Result recoverRole(String id, Integer status) {
        try {
            BaseAdminRole role = findRoleById(id);
            role.setRoleStatus(status);
            this.baseAdminRoleDao.save(role);
            return ResultUtils.success("?????????????????????");
        } catch (Exception e) {
            log.error("?????????????????????{}", e);
            return ResultUtils.fail("?????????????????????");
        }
    }

    @Override
    public List<BaseAdminRole> getRoles() {
        return baseAdminRoleDao.findByRoleStatus(1);
    }
}
