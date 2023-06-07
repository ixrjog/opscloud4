package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "ser_deploy_task")
public class SerDeployTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 应用ID
     */
    @Column(name = "application_id")
    private Integer applicationId;

    /**
     * 任务UUID
     */
    @Column(name = "task_uuid")
    private String taskUuid;

    /**
     * 任务名称
     */
    @Column(name = "task_name")
    private String taskName;

    /**
     * 任务描述
     */
    @Column(name = "task_desc")
    private String taskDesc;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Boolean isActive;

    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private String taskStatus;

    /**
     * 任务结果
     */
    @Column(name = "task_result")
    private String taskResult;

    /**
     * 是否已完成任务
     */
    @Column(name = "is_finish")
    private Boolean isFinish;

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
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;
}