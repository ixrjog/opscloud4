package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_change_task")
public class OcServerChangeTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务唯一id
     */
    @Column(name = "task_id")
    private String taskId;

    /**
     * 服务器id
     */
    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 服务器组id
     */
    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 变更类型
     */
    @Column(name = "change_type")
    private String changeType;

    /**
     * 变更数量
     */
    @Column(name = "change_number")
    private Integer changeNumber;

    /**
     * 当前变更流程id
     */
    @Column(name = "task_flow_id")
    private Integer taskFlowId;

    /**
     * 流程名称
     */
    @Column(name = "task_flow_name")
    private String taskFlowName;

    /**
     * 任务返回值
     */
    @Column(name = "result_code")
    private Integer resultCode;

    /**
     * 任务信息
     */
    @Column(name = "result_msg")
    private String resultMsg;

    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private Integer taskStatus;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

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
     * 获取任务唯一id
     *
     * @return task_id - 任务唯一id
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 设置任务唯一id
     *
     * @param taskId 任务唯一id
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取服务器id
     *
     * @return server_id - 服务器id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * 设置服务器id
     *
     * @param serverId 服务器id
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取服务器组id
     *
     * @return server_group_id - 服务器组id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * 设置服务器组id
     *
     * @param serverGroupId 服务器组id
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    /**
     * 获取变更类型
     *
     * @return change_type - 变更类型
     */
    public String getChangeType() {
        return changeType;
    }

    /**
     * 设置变更类型
     *
     * @param changeType 变更类型
     */
    public void setChangeType(String changeType) {
        this.changeType = changeType;
    }

    /**
     * 获取变更数量
     *
     * @return change_number - 变更数量
     */
    public Integer getChangeNumber() {
        return changeNumber;
    }

    /**
     * 设置变更数量
     *
     * @param changeNumber 变更数量
     */
    public void setChangeNumber(Integer changeNumber) {
        this.changeNumber = changeNumber;
    }

    /**
     * 获取当前变更流程id
     *
     * @return task_flow_id - 当前变更流程id
     */
    public Integer getTaskFlowId() {
        return taskFlowId;
    }

    /**
     * 设置当前变更流程id
     *
     * @param taskFlowId 当前变更流程id
     */
    public void setTaskFlowId(Integer taskFlowId) {
        this.taskFlowId = taskFlowId;
    }

    /**
     * 获取流程名称
     *
     * @return task_flow_name - 流程名称
     */
    public String getTaskFlowName() {
        return taskFlowName;
    }

    /**
     * 设置流程名称
     *
     * @param taskFlowName 流程名称
     */
    public void setTaskFlowName(String taskFlowName) {
        this.taskFlowName = taskFlowName;
    }

    /**
     * 获取任务返回值
     *
     * @return result_code - 任务返回值
     */
    public Integer getResultCode() {
        return resultCode;
    }

    /**
     * 设置任务返回值
     *
     * @param resultCode 任务返回值
     */
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * 获取任务信息
     *
     * @return result_msg - 任务信息
     */
    public String getResultMsg() {
        return resultMsg;
    }

    /**
     * 设置任务信息
     *
     * @param resultMsg 任务信息
     */
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    /**
     * 获取任务状态
     *
     * @return task_status - 任务状态
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置任务状态
     *
     * @param taskStatus 任务状态
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * @return start_time
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * @param startTime
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * @return end_time
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * @param endTime
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
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
}