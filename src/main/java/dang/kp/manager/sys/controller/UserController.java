package dang.kp.manager.sys.controller;

import com.google.common.collect.Lists;
import dang.kp.manager.common.response.PageDataResult;
import dang.kp.manager.common.result.Result;
import dang.kp.manager.sys.dto.LoginDTO;
import dang.kp.manager.sys.dto.UserSearchDTO;
import dang.kp.manager.sys.pojo.BaseAdminUser;
import dang.kp.manager.sys.pojo.DictItem;
import dang.kp.manager.sys.service.AdminUserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Title: UserController
 * @Description: 系统用户管理
 * @author: youqing
 * @version: 1.0
 * @date: 2018/11/20 15:17
 */
@Slf4j
@Controller
@RequestMapping("user")
public class UserController {

    @Autowired
    private AdminUserService adminUserService;

    /**
     * 功能描述: 登入系统
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 15:47
     */
    @RequestMapping("login")
    @ResponseBody
    public Map<String, Object> login(HttpServletRequest request, LoginDTO loginDTO, HttpSession session) {
        log.info("进行登陆");
        Map<String, Object> data = new HashMap();
        // 使用 shiro 进行登录
        Subject subject = SecurityUtils.getSubject();

        String userName = loginDTO.getUsername().trim();
        String password = loginDTO.getPassword().trim();
        String rememberMe = loginDTO.getRememberMe();
        String host = request.getRemoteAddr();

        //获取token
        UsernamePasswordToken token = new UsernamePasswordToken(userName, password, host);

        // 设置 remenmberMe 的功能
        if (rememberMe != null && rememberMe.equals("on")) {
            token.setRememberMe(true);
        }

        try {
            subject.login(token);
            // 登录成功
            BaseAdminUser user = (BaseAdminUser) subject.getPrincipal();

            session.setAttribute("user", user.getSysUserName());
            data.put("code", 200);
            data.put("url", "/home");
            //data.put("message","登陆成功");
            log.info(user.getSysUserName() + "登陆成功");
        } catch (UnknownAccountException e) {
            data.put("code", 0);
            data.put("message", userName + "账号不存在");
            log.error(userName + "账号不存在");
            return data;
        } catch (DisabledAccountException e) {
            data.put("code", 0);
            data.put("message", userName + "账号异常");
            log.error(userName + "账号异常");
            return data;
        } catch (AuthenticationException e) {
            data.put("code", 0);
            data.put("message", userName + "密码错误");
            log.error(userName + "密码错误");
            return data;
        }

        return data;
    }

    /**
     * 功能描述: 修改密码
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 17:26
     */
    @RequestMapping("setPwd")
    @ResponseBody
    public Map<String, Object> setP(String pwd, String isPwd) {
        log.info("进行密码重置");
        Map<String, Object> data = new HashMap();
        if (!pwd.equals(isPwd)) {
            data.put("code", 0);
            data.put("message", "两次输入的密码不一致!");
            log.error("两次输入的密码不一致!");
            return data;
        }
        //获取当前登陆的用户信息
        BaseAdminUser user = (BaseAdminUser) SecurityUtils.getSubject().getPrincipal();
        int result = adminUserService.updatePwd(user.getSysUserName(), pwd);
        if (result == 0) {
            data.put("code", 0);
            data.put("msg", "修改密码失败！");
            log.error("用户修改密码失败！");
            return data;
        }
        data.put("code", 1);
        data.put("msg", "修改密码成功！");
        log.info("用户修改密码成功！");
        return data;
    }

    /**
     * 功能描述: 跳到系统用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 13:50
     */
    @RequestMapping("/userManage")
    public String userManage(ModelMap model) {
        log.info("跳到系统用户列表");
        List<DictItem> roleList = Lists.newArrayList(DictItem.builder().text("请选择").value("").build(),
                DictItem.builder().text("dangkp").value("1").build(),
                DictItem.builder().text("ACN").value("0").build());
        model.put("roleList", roleList);
        return "/sys/user/userManage";
    }

    /**
     * 功能描述: 分页查询用户列表
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/21 11:10
     */
    @RequestMapping(value = "/getUserList", method = RequestMethod.POST)
    @ResponseBody
    public PageDataResult getUserList(@RequestParam("pageNum") Integer pageNum,
                                      @RequestParam("pageSize") Integer pageSize,/*@Valid PageRequest page,*/ UserSearchDTO userSearch) {
        /*log.info("分页查询用户列表！搜索条件：userSearch：" + userSearch + ",pageNum:" + page.getPageNum()
                + ",每页记录数量pageSize:" + page.getPageSize());*/
        PageDataResult pdr = new PageDataResult();
        try {
            if (null == pageNum) {
                pageNum = 1;
            }
            if (null == pageSize) {
                pageSize = 10;
            }
            // 获取用户列表
            pdr = adminUserService.getUserList(userSearch, pageNum, pageSize);
            log.info("用户列表查询=pdr:" + pdr);

        } catch (Exception e) {
            e.printStackTrace();
            log.error("用户列表查询异常！", e);
        }
        return pdr;
    }


    /**
     * 功能描述: 新增和更新系统用户
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 10:14
     */
    @RequestMapping(value = "/setUser", method = RequestMethod.POST)
    @ResponseBody
    public Result setUser(BaseAdminUser user) {
        log.info("设置用户[新增或更新]！user:" + user);
        Map<String, Object> data = new HashMap();
        if (StringUtils.isBlank(user.getId())) {
            return adminUserService.addUser(user);
        } else {
            return adminUserService.updateUser(user);
        }
    }


    /**
     * 功能描述: 删除/恢复 用户
     *
     * @param:
     * @return:
     * @auther: youqing
     * @date: 2018/11/22 11:59
     */
    @RequestMapping(value = "/updateUserStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result updateUserStatus(@RequestParam("id") String id, @RequestParam("status") Integer status) {
        log.info("删除/恢复用户！id:" + id + " status:" + status);
        Map<String, Object> data = new HashMap<>();
        if (status == 0) {
            //删除用户
            return adminUserService.delUser(id, status);
        } else {
            //恢复用户
            return adminUserService.recoverUser(id, status);
        }
    }
}
