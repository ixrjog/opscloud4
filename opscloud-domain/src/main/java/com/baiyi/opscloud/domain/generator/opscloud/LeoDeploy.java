package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "leo_deploy")
public class LeoDeploy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用ID
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 任务ID
     */
    @Column(name = "job_id")
    private Integer jobId;

    /**
     * 任务名称
     */
    @Column(name = "job_name")
    private String jobName;

    /**
     * 部署编号
     */
    @Column(name = "deploy_number")
    private Integer deployNumber;

    /**
     * 构建ID
     */
    @Column(name = "build_id")
    private Integer buildId;

    /**
     * 资产ID
     */
    @Column(name = "asset_id")
    private Integer assetId;

    /**
     * 版本名称
     */
    @Column(name = "version_name")
    private String versionName;

    /**
     * 版本描述
     */
    @Column(name = "version_desc")
    private String versionDesc;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 部署状态
     */
    @Column(name = "deploy_status")
    private String deployStatus;

    /**
     * 部署结果
     */
    @Column(name = "deploy_result")
    private String deployResult;

    /**
     * 是否已完成任务
     */
    @Column(name = "is_finish")
    private Boolean isFinish;

    /**
     * 执行类型
     */
    @Column(name = "execution_type")
    private Integer executionType;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 回滚操作
     */
    @Column(name = "is_rollback")
    private Boolean isRollback;

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
     * 部署配置
     */
    @Column(name = "deploy_config")
    private String deployConfig;

    @Column(name = "oc_instance")
    private String ocInstance;

    @Column(name = "project_id")
    private Integer projectId;

    /**
     * 描述
     */
    private String comment;
}