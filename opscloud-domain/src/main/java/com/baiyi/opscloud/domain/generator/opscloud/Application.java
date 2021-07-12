package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用名称
     */
    private String name;

    /**
     * 应用key
     */
    @Column(name = "application_key")
    private String applicationKey;

    /**
     * 应用类型
     */
    @Column(name = "application_type")
    private Integer applicationType;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 描述
     */
    private String comment;
}