package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_change_task_flow")
public class OcServerChangeTaskFlow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务唯一id
     */
    @Column(name = "task_id")
    private String taskId;

    /**
     * 父流程id
     */
    @Column(name = "flow_parent_id")
    private Integer flowParentId;

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

    /**
     * 外部id
     */
    @Column(name = "external_id")
    private Integer externalId;

    /**
     * 外部类型
     */
    @Column(name = "external_type")
    private String externalType;

    @Column(name = "start_time")
    private Date startTime;

    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 任务详情
     */
    @Column(name = "task_detail")
    private String taskDetail;

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
     * 获取父流程id
     *
     * @return flow_parent_id - 父流程id
     */
    public Integer getFlowParentId() {
        return flowParentId;
    }

    /**
     * 设置父流程id
     *
     * @param flowParentId 父流程id
     */
    public void setFlowParentId(Integer flowParentId) {
        this.flowParentId = flowParentId;
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
     * 获取外部id
     *
     * @return external_id - 外部id
     */
    public Integer getExternalId() {
        return externalId;
    }

    /**
     * 设置外部id
     *
     * @param externalId 外部id
     */
    public void setExternalId(Integer externalId) {
        this.externalId = externalId;
    }

    /**
     * 获取外部类型
     *
     * @return external_type - 外部类型
     */
    public String getExternalType() {
        return externalType;
    }

    /**
     * 设置外部类型
     *
     * @param externalType 外部类型
     */
    public void setExternalType(String externalType) {
        this.externalType = externalType;
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

    /**
     * 获取任务详情
     *
     * @return task_detail - 任务详情
     */
    public String getTaskDetail() {
        return taskDetail;
    }

    /**
     * 设置任务详情
     *
     * @param taskDetail 任务详情
     */
    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }
}