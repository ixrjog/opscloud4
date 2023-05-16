package com.baiyi.opscloud.domain.generator.opscloud;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leo_job")
public class LeoJob implements Serializable, BaseBusiness.IBusiness {

    @Serial
    private static final long serialVersionUID = -37689929767342534L;

    @Override
    public Integer getBusinessId() {
        return this.getId();
    }

    @Override
    public Integer getBusinessType() {
        return BusinessTypeEnum.LEO_JOB.getType();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 关联任务ID
     */
    @Column(name = "parent_id")
    private Integer parentId;

    /**
     * 应用ID
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 显示名称
     */
    private String name;

    /**
     * 任务Key(不可变名称)
     */
    @Column(name = "job_key")
    private String jobKey;

    @Column(name = "build_type")
    private String buildType;

    /**
     * 默认分支
     */
    private String branch;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 当前构建编号
     */
    @Column(name = "build_number")
    private Integer buildNumber;

    /**
     * 模板版本
     */
    @Column(name = "template_version")
    private String templateVersion;

    /**
     * 隐藏任务
     */
    private Boolean hide;

    /**
     * 模板ID
     */
    @Column(name = "template_id")
    private Integer templateId;

    /**
     * 任务超文本链接
     */
    private String href;

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
     * 任务参数配置
     */
    @Column(name = "job_config")
    private String jobConfig;

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