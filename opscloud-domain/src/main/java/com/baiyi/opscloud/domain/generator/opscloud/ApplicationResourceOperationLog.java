package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@Table(name = "application_resource_operation_log")
public class ApplicationResourceOperationLog {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源ID
     */
    @Column(name = "resource_id")
    private Integer resourceId;

    /**
     * 资源类型
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 资源环境
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 操作用户名
     */
    private String username;

    /**
     * 是否管理员操作
     */
    @Column(name = "is_admin")
    private Boolean isAdmin;

    /**
     * 操作类型
     */
    @Column(name = "operation_type")
    private String operationType;

    /**
     * 操作时间
     */
    @Column(name = "operation_time")
    private Date operationTime;

    /**
     * 操作结果
     */
    private String result;

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

    /**
     * 描述
     */
    private String comment;
}