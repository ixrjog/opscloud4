package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_instance_delete_queue")
public class OcCloudInstanceDeleteQueue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 任务id
     */
    @Column(name = "task_id")
    private Integer taskId;

    /**
     * 实例id
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 任务执行状态, 2:队列中 1:执行中 0:结束
     */
    @Column(name = "task_status")
    private Integer taskStatus;

    /**
     * 任务执行返回值, 0:成功 !0:错误
     */
    @Column(name = "result_code")
    private Integer resultCode;

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
     * 获取任务id
     *
     * @return task_id - 任务id
     */
    public Integer getTaskId() {
        return taskId;
    }

    /**
     * 设置任务id
     *
     * @param taskId 任务id
     */
    public void setTaskId(Integer taskId) {
        this.taskId = taskId;
    }

    /**
     * 获取实例id
     *
     * @return instance_id - 实例id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例id
     *
     * @param instanceId 实例id
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 获取任务执行状态, 2:队列中 1:执行中 0:结束
     *
     * @return task_status - 任务执行状态, 2:队列中 1:执行中 0:结束
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置任务执行状态, 2:队列中 1:执行中 0:结束
     *
     * @param taskStatus 任务执行状态, 2:队列中 1:执行中 0:结束
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 获取任务执行返回值, 0:成功 !0:错误
     *
     * @return result_code - 任务执行返回值, 0:成功 !0:错误
     */
    public Integer getResultCode() {
        return resultCode;
    }

    /**
     * 设置任务执行返回值, 0:成功 !0:错误
     *
     * @param resultCode 任务执行返回值, 0:成功 !0:错误
     */
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
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