package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Table(name = "datasource_instance")
public class DatasourceInstance implements Serializable {

    @Serial
    private static final long serialVersionUID = 6848520019487364982L;

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
    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;
}