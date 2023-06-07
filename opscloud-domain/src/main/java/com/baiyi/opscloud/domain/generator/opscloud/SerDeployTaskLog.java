package com.baiyi.opscloud.domain.generator.opscloud;

import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
@Table(name = "ser_deploy_task_log")
public class SerDeployTaskLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务ID
     */
    @Column(name = "ser_deploy_task_id")
    private Integer serDeployTaskId;

    /**
     * 日志类型
     */
    @Column(name = "log_type")
    private String logType;

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
     * 请求内容
     */
    @Column(name = "request_content")
    private String requestContent;

    /**
     * 响应内容
     */
    @Column(name = "response_content")
    private String responseContent;
}