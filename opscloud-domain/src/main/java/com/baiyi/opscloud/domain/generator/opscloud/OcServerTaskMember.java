package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_task_member")
public class OcServerTaskMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 主机模式
     */
    @Column(name = "host_pattern")
    private String hostPattern;

    @Column(name = "server_id")
    private Integer serverId;

    @Column(name = "env_type")
    private Integer envType;

    /**
     * 管理ip
     */
    @Column(name = "manage_ip")
    private String manageIp;

    private String comment;

    /**
     * 是否完成
     */
    private Integer finalized;

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

    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "task_result")
    private String taskResult;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "output_msg")
    private String outputMsg;

    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * @return id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return task_id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * @param taskId
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取主机模式
     *
     * @return host_pattern - 主机模式
     */
    public String getHostPattern() {
        return hostPattern;
    }

    /**
     * 设置主机模式
     *
     * @param hostPattern 主机模式
     */
    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
    }

    /**
     * @return server_id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * @param serverId
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    public Integer getEnvType() {
        return envType;
    }

    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    /**
     * 获取管理ip
     *
     * @return manage_ip - 管理ip
     */
    public String getManageIp() {
        return manageIp;
    }

    /**
     * 设置管理ip
     *
     * @param manageIp 管理ip
     */
    public void setManageIp(String manageIp) {
        this.manageIp = manageIp;
    }

    /**
     * @return comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取是否完成
     *
     * @return finalized - 是否完成
     */
    public Integer getFinalized() {
        return finalized;
    }

    /**
     * 设置是否完成
     *
     * @param finalized 是否完成
     */
    public void setFinalized(Integer finalized) {
        this.finalized = finalized;
    }

    /**
     * 获取终止任务
     *
     * @return stop_type - 终止任务
     */
    public Integer getStopType() {
        return stopType;
    }

    /**
     * 设置终止任务
     *
     * @param stopType 终止任务
     */
    public void setStopType(Integer stopType) {
        this.stopType = stopType;
    }

    /**
     * 获取退出值
     *
     * @return exit_value - 退出值
     */
    public Integer getExitValue() {
        return exitValue;
    }

    /**
     * 设置退出值
     *
     * @param exitValue 退出值
     */
    public void setExitValue(Integer exitValue) {
        this.exitValue = exitValue;
    }

    /**
     * 获取任务状态
     *
     * @return task_status - 任务状态
     */
    public String getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置任务状态
     *
     * @param taskStatus 任务状态
     */
    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * @return task_result
     */
    public String getTaskResult() {
        return taskResult;
    }

    /**
     * @param taskResult
     */
    public void setTaskResult(String taskResult) {
        this.taskResult = taskResult;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * @return output_msg
     */
    public String getOutputMsg() {
        return outputMsg;
    }

    /**
     * @param outputMsg
     */
    public void setOutputMsg(String outputMsg) {
        this.outputMsg = outputMsg;
    }

    /**
     * @return error_msg
     */
    public String getErrorMsg() {
        return errorMsg;
    }

    /**
     * @param errorMsg
     */
    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}