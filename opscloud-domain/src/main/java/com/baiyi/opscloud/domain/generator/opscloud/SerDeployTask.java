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
     * 是否已完成任务
     */
    @Column(name = "is_finish")
    private Boolean isFinish;

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