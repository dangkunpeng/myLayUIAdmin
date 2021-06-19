package dang.kp.manager.sys.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "base_admin_user")
public class BaseAdminUser {
    /**
     * ID
     */
    @Id
    private String id;

    /**
     * 系统用户名称
     */
    @Column(name = "sys_user_name")
    private String sysUserName;

    /**
     * 系统用户密码
     */
    @Column(name = "sys_user_pwd")
    private String sysUserPwd;

    /**
     * 角色
     */
    @Column(name = "role_id")
    private String roleId;

    /**
     * 手机号
     */
    @Column(name = "user_phone")
    private String userPhone;

    /**
     * 登记时间
     */
    @Column(name = "reg_time")
    private String regTime;

    /**
     * 状态（0：无效；1：有效）
     */
    @Column(name = "user_status")
    private Integer userStatus;
}