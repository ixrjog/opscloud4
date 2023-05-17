package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.BaseProjectResource;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "project_resource")
public class ProjectResource implements BaseProjectResource.IProjectResource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 项目ID
     */
    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 资源名称
     */
    private String name;

    /**
     * 虚拟资源
     */
    @Column(name = "virtual_resource")
    private Boolean virtualResource;

    /**
     * 资源类型
     */
    @Column(name = "resource_type")
    private String resourceType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 角色
     */
    private String role;

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