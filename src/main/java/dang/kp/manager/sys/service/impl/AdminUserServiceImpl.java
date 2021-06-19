package dang.kp.manager.sys.service.impl;

import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.common.result.ResultUtils;
import dang.kp.manager.common.utils.BatchUtils;
import dang.kp.manager.common.utils.DateUtils;
import dang.kp.manager.common.utils.DigestUtils;
import dang.kp.manager.common.utils.MyConstants;
import dang.kp.manager.sys.dao.BaseAdminUserDao;
import dang.kp.manager.sys.dto.UserSearchDTO;
import dang.kp.manager.sys.pojo.BaseAdminUser;
import dang.kp.manager.sys.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Title: AdminUserServiceImpl
 * @Description:
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/21 11:04
 */
@Slf4j
@Service
public class AdminUserServiceImpl implements AdminUserService {

    @Autowired
    private BaseAdminUserDao baseAdminUserDao;

    @Override
    public PageDataResult getUserList(UserSearchDTO userSearch, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        // 匹配模式
        ExampleMatcher matcher = ExampleMatcher.matching()
                .withMatcher("sysUserName", ExampleMatcher.GenericPropertyMatchers.contains())
                .withMatcher("userPhone", ExampleMatcher.GenericPropertyMatchers.contains())

                .withIgnoreNullValues();
        BaseAdminUser user = new BaseAdminUser();
        BeanUtils.copyProperties(userSearch, user);
        // 查询模板
        Example<BaseAdminUser> example = Example.of(user, matcher);
        Page<BaseAdminUser> pageResult = this.baseAdminUserDao.findAll(example, pageable);
        return PageDataResult.builder().list(pageResult.getContent()).totals(pageResult.getTotalPages()).code(200).build();
    }


    @Override
    public Result addUser(BaseAdminUser user) {
        Map<String, Object> data = new HashMap();
        try {
            if (StringUtils.length(user.getUserPhone()) != 11) {
                log.error("置用户[新增或更新]，结果=手机号位数不对！");
                return ResultUtils.fail("手机号位数不！");
            }
            String userName = user.getSysUserName();

            BaseAdminUser old = findByUserName(userName);
            if (Objects.nonNull(old)) {
                log.error("用户[新增]，结果=用户名已存在！");
                return ResultUtils.fail("用户名已存在！");
            }

            if (StringUtils.isBlank(user.getSysUserPwd())) {
                user.setSysUserPwd("123456");
            }
            String password = DigestUtils.Md5(userName, user.getSysUserPwd());
            user.setSysUserPwd(password);

            user.setRegTime(DateUtils.getCurrentDate());
            user.setUserStatus(1);
            user.setId(BatchUtils.getKey(MyConstants.MyKey.BaseAdminUser));
            this.baseAdminUserDao.save(user);
            log.info("用户[新增]，结果=新增成功！");
        } catch (Exception e) {
            log.error("用户[新增]异常！{}", e);
            return ResultUtils.fail("用户[新增]异常！");
        }
        return ResultUtils.success("新增成功！");
    }


    @Override
    public Result updateUser(BaseAdminUser user) {
        Map<String, Object> data = new HashMap();
        String id = user.getId();
        String username = user.getSysUserName();
        Optional<BaseAdminUser> old = this.baseAdminUserDao.findById(user.getId());
        if (!old.isPresent()) {
            log.info("用户[更新]，结果=更新失败！");
            return ResultUtils.fail("更新失败！");
        }
        if (StringUtils.isNotBlank(user.getSysUserPwd())) {
            String password = DigestUtils.Md5(username, user.getSysUserPwd());
            user.setSysUserPwd(password);
        }
        BaseAdminUser update = old.get();
        BeanUtils.copyProperties(user, update);
        this.baseAdminUserDao.save(update);
        log.info("用户[更新]，结果=更新成功！");
        return ResultUtils.success("更新成功！");
    }

    @Override
    public BaseAdminUser getUserById(String id) {
        return this.baseAdminUserDao.findById(id).get();
    }


    @Override
    public Result delUser(String id, Integer status) {
        try {
            // 删除用户
            BaseAdminUser user = this.baseAdminUserDao.findById(id).get();
            user.setUserStatus(status);
            this.baseAdminUserDao.save(user);
        } catch (Exception e) {
            log.error("删除用户异常！{}", e);
        }
        return ResultUtils.success("删除用户成功");
    }

    @Override
    public Result recoverUser(String id, Integer status) {
        try {
            BaseAdminUser user = this.baseAdminUserDao.findById(id).get();
            user.setUserStatus(status);
            this.baseAdminUserDao.save(user);
        } catch (Exception e) {
            log.error("恢复用户异常！{}", e);
        }
        return ResultUtils.success("恢复用户成功");
    }

    @Override
    public BaseAdminUser findByUserName(String userName) {
        return this.baseAdminUserDao.getUserBySysUserNameAndUserStatus(userName, 1);
    }

    @Override
    public int updatePwd(String userName, String password) {
        password = DigestUtils.Md5(userName, password);
        BaseAdminUser user = findByUserName(userName);
        user.setSysUserPwd(password);
        this.baseAdminUserDao.save(user);
        return 1;
    }
}
