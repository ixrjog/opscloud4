package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "server_task")
public class ServerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 数据源实例uuid
     */
    @Column(name = "instance_uuid")
    private String instanceUuid;

    /**
     * 任务uuid
     */
    @Column(name = "task_uuid")
    private String taskUuid;

    /**
     * 用户名
     */
    private String username;

    /**
     * 成员数量
     */
    @Column(name = "member_size")
    private Integer memberSize;

    /**
     * 剧本id
     */
    @Column(name = "ansible_playbook_id")
    private Integer ansiblePlaybookId;

    /**
     * PLAYBOOK
     */
    @Column(name = "task_type")
    private String taskType;

    /**
     * 是否完成
     */
    private Boolean finalized;

    /**
     * 终止任务
     */
    @Column(name = "stop_type")
    private Integer stopType;

    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private String taskStatus;

    /**
     * 任务开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 任务结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String vars;

    private String tags;
}