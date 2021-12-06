package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "business_template")
public class BusinessTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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
     * 模版ID
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
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 模版变量
     */
    private String vars;

    /**
     * 模版内容
     */
    private String content;

    /**
     * 描述
     */
    private String comment;
}