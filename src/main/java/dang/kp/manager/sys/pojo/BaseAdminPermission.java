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
@Table(name = "base_admin_permission")
public class BaseAdminPermission {
    @Id
    private String id;

    /**
     * 菜单名称
     */
    @Column
    private String name;

    /**
     * 父菜单id
     */
    @Column
    private String pid;

    /**
     * 描述
     */
    @Column
    private String descpt;

    /**
     * 菜单url
     */
    @Column
    private String url;

    /**
     * 添加时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private String updateTime;

    /**
     * 删除标志（0:删除 1：存在）
     */
    @Column(name = "del_flag")
    private Integer delFlag;

}