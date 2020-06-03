package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_instance_task")
public class OcCloudInstanceTask {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cloud_type")
    private Integer cloudType;

    /**
     * 模版id
     */
    @Column(name = "template_id")
    private Integer templateId;

    @Column(name = "user_id")
    private Integer userId;

    @Column(name = "region_id")
    private String regionId;

    /**
     * 创建数量
     */
    @Column(name = "create_size")
    private Integer createSize;

    @Column(name = "task_phase")
    private String taskPhase;

    /**
     * 任务状态
     */
    @Column(name = "task_status")
    private String taskStatus;

    @Column(name = "error_msg")
    private String errorMsg;

    /**
     * 任务类型
     */
    @Column(name = "task_type")
    private Integer taskType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    @Column(name = "user_detail")
    private String userDetail;

    /**
     * 任务详情
     */
    @Column(name = "create_instance")
    private String createInstance;

    private String comment;

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
     * @return cloud_type
     */
    public Integer getCloudType() {
        return cloudType;
    }

    /**
     * @param cloudType
     */
    public void setCloudType(Integer cloudType) {
        this.cloudType = cloudType;
    }

    /**
     * 获取模版id
     *
     * @return template_id - 模版id
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * 设置模版id
     *
     * @param templateId 模版id
     */
    public void setTemplateId(Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * @return user_id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return region_id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * @param regionId
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取创建数量
     *
     * @return create_size - 创建数量
     */
    public Integer getCreateSize() {
        return createSize;
    }

    /**
     * 设置创建数量
     *
     * @param createSize 创建数量
     */
    public void setCreateSize(Integer createSize) {
        this.createSize = createSize;
    }

    /**
     * @return task_phase
     */
    public String getTaskPhase() {
        return taskPhase;
    }

    /**
     * @param taskPhase
     */
    public void setTaskPhase(String taskPhase) {
        this.taskPhase = taskPhase;
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

    /**
     * 获取任务类型
     *
     * @return task_type - 任务类型
     */
    public Integer getTaskType() {
        return taskType;
    }

    /**
     * 设置任务类型
     *
     * @param taskType 任务类型
     */
    public void setTaskType(Integer taskType) {
        this.taskType = taskType;
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
     * @return user_detail
     */
    public String getUserDetail() {
        return userDetail;
    }

    /**
     * @param userDetail
     */
    public void setUserDetail(String userDetail) {
        this.userDetail = userDetail;
    }

    /**
     * 获取任务详情
     *
     * @return create_instance - 任务详情
     */
    public String getCreateInstance() {
        return createInstance;
    }

    /**
     * 设置任务详情
     *
     * @param createInstance 任务详情
     */
    public void setCreateInstance(String createInstance) {
        this.createInstance = createInstance;
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
}