package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "sys_nav")
public class Nav {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 导航标题
     */
    @Column(name = "nav_title")
    private String navTitle;

    /**
     * 导航链接
     */
    @Column(name = "nav_url")
    private String navUrl;

    /**
     * 导航说明
     */
    @Column(name = "nav_content")
    private String navContent;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;
}