package com.baiyi.caesar.domain.generator.caesar;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "datasource_instance")
public class DatasourceInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源关联id
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 数据源名称
     */
    @Column(name = "instance_name")
    private String instanceName;

    /**
     * UUID
     */
    private String uuid;

    /**
     * 实例类型
     */
    @Column(name = "instance_type")
    private String instanceType;

    /**
     * 实例分类
     */
    private String kind;

    /**
     * 实例版本
     */
    private String version;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 数据源配置id
     */
    @Column(name = "config_id")
    private Integer configId;

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

    private String comment;
}