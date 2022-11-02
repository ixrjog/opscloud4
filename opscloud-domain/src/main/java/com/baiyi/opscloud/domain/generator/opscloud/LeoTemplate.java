package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leo_template")
public class LeoTemplate implements Serializable {

    private static final long serialVersionUID = -517321853690494654L;
    
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
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 更新时间
     */
    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 创建时间
     */
    @Column(name = "create_time", insertable = false, updatable = false)
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
     * 模板内容
     */
    @Column(name = "template_content")
    private String templateContent;

    /**
     * 描述
     */
    private String comment;
}