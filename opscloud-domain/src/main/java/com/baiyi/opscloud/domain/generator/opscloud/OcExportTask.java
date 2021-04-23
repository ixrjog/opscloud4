package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_export_task")
public class OcExportTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 导出文件名
     */
    @Column(name = "file_name")
    private String fileName;

    /**
     * 导出类型
     * 1:资产列表
     * 2:资产派发
     */
    @Column(name = "task_type")
    private Integer taskType;

    /**
     * 导出状态
     * 0:失败
     * 1:进行中
     * 2:完成
     */
    @Column(name = "task_status")
    private Integer taskStatus;

    /**
     * 导出人id
     */
    private String username;

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
     * 获取导出文件名
     *
     * @return file_name - 导出文件名
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * 设置导出文件名
     *
     * @param fileName 导出文件名
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * 获取导出类型
     * 1:资产列表
     * 2:资产派发
     *
     * @return task_type - 导出类型
     * 1:资产列表
     * 2:资产派发
     */
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * 设置导出类型
     * 1:资产列表
     * 2:资产派发
     *
     * @param taskType 导出类型
     *                 1:资产列表
     *                 2:资产派发
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
    }

    /**
     * 获取导出状态
     * 0:失败
     * 1:进行中
     * 2:完成
     *
     * @return task_status - 导出状态
     * 0:失败
     * 1:进行中
     * 2:完成
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 设置导出状态
     * 0:失败
     * 1:进行中
     * 2:完成
     *
     * @param taskStatus 导出状态
     *                   0:失败
     *                   1:进行中
     *                   2:完成
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 获取导出人id
     *
     * @return username - 导出人id
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置导出人id
     *
     * @param username 导出人id
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取任务开始时间
     *
     * @return start_time - 任务开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置任务开始时间
     *
     * @param startTime 任务开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取任务结束时间
     *
     * @return end_time - 任务结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置任务结束时间
     *
     * @param endTime 任务结束时间
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