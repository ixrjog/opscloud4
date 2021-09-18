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
@Table(name = "server_task_member")
public class ServerTaskMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务id
     */
    @Column(name = "server_task_id")
    private Integer serverTaskId;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 服务器名称
     */
    @Column(name = "server_name")
    private String serverName;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 管理ip
     */
    @Column(name = "manage_ip")
    private String manageIp;

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
     * 退出值
     */
    @Column(name = "exit_value")
    private Integer exitValue;

    @Column(name = "task_result")
    private String taskResult;

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

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    @Column(name = "output_msg")
    private String outputMsg;

    @Column(name = "error_msg")
    private String errorMsg;
}