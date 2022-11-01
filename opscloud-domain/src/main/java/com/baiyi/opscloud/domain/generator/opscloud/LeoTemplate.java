package com.baiyi.opscloud.domain.generator.opscloud;

import java.util.Date;
import javax.persistence.*;
import lombok.Data;

@Data
@Table(name = "leo_template")
public class LeoTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 实例UUID
     */
    @Column(name = "jenkins_instance_uuid")
    private String jenkinsInstanceUuid;

    /**
     * 模版名称
     */
    @Column(name = "template_name")
    private String templateName;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 模板配置
     */
    @Column(name = "template_config")
    private String templateConfig;

    /**
     * 模板参数
     */
    @Column(name = "template_parameter")
    private String templateParameter;

    /**
     * 描述
     */
    private String comment;
}