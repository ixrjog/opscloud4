package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "business_template")
public class BusinessTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    /**
     * 实例UUID
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 业务ID
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 模板ID
     */
    @Column(name = "template_id")
    private Integer templateId;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

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
     * 模板变量
     */
    private String vars;

    private String kind;

    /**
     * 模板内容
     */
    private String content;

    /**
     * 描述
     */
    private String comment;
}