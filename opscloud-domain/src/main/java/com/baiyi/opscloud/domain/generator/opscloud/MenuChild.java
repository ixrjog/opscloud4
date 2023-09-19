package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_menu_child")
public class MenuChild {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 菜单id
     */
    @Column(name = "menu_id")
    private Integer menuId;

    /**
     * 子菜单标题
     */
    private String title;

    @Column(name = "i18n_en")
    private String i18nEn;

    /**
     * 子菜单图标名称
     */
    private String icon;

    /**
     * 子菜单路径
     */
    private String path;

    /**
     * 子菜单排序
     */
    private Integer seq;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}