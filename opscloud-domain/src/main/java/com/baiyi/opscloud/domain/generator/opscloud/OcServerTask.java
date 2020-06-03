package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_task")
public class OcServerTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 资源组id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 0:命令 1:脚本 2:playbook
     */
    @Column(name = "task_type")
    private Integer taskType;

    /**
     * 系统任务类型
     */
    @Column(name = "system_type")
    private Integer systemType;

    private String comment;

    @Column(name = "task_size")
    private Integer taskSize;

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

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "executor_param")
    private String executorParam;

    /**
     * 资源名称
     */
    @Column(name = "user_detail")
    private String userDetail;

    /**
     * 目标
     */
    @Column(name = "server_target_detail")
    private String serverTargetDetail;

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
     * 获取资源组id
     *
     * @return user_id - 资源组id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置资源组id
     *
     * @param userId 资源组id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取0:命令 1:脚本 2:playbook
     *
     * @return task_type - 0:命令 1:脚本 2:playbook
     */
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * 设置0:命令 1:脚本 2:playbook
     *
     * @param taskType 0:命令 1:脚本 2:playbook
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取系统任务类型
     *
     * @return system_type - 系统任务类型
     */
    public Integer getSystemType() {
        return systemType;
    }

    /**
     * 设置系统任务类型
     *
     * @param systemType 系统任务类型
     */
    public void setSystemType(Integer systemType) {
        this.systemType = systemType;
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
     * @return task_size
     */
    public Integer getTaskSize() {
        return taskSize;
    }

    /**
     * @param taskSize
     */
    public void setTaskSize(Integer taskSize) {
        this.taskSize = taskSize;
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
     * @return executor_param
     */
    public String getExecutorParam() {
        return executorParam;
    }

    /**
     * @param executorParam
     */
    public void setExecutorParam(String executorParam) {
        this.executorParam = executorParam;
    }

    /**
     * 获取资源名称
     *
     * @return user_detail - 资源名称
     */
    public String getUserDetail() {
        return userDetail;
    }

    /**
     * 设置资源名称
     *
     * @param userDetail 资源名称
     */
    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    /**
     * 获取目标
     *
     * @return server_target_detail - 目标
     */
    public String getServerTargetDetail() {
        return serverTargetDetail;
    }

    /**
     * 设置目标
     *
     * @param serverTargetDetail 目标
     */
    public void setServerTargetDetail(String serverTargetDetail) {
        this.serverTargetDetail = serverTargetDetail;
    }
}