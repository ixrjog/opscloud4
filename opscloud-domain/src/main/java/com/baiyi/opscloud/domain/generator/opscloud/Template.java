package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Template {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 名称
     */
    private String name;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 实例类型
     */
    @Column(name = "instance_type")
    private String instanceType;

    /**
     * 模版key
     */
    @Column(name = "template_key")
    private String templateKey;

    /**
     * 模版类型
     */
    @Column(name = "template_type")
    private String templateType;

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