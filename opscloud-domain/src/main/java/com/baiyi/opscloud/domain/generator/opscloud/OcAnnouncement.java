package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_announcement")
public class OcAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告类型
     * 0:新功能 Features
     * 1:日常通知
     */
    private Integer type;

    /**
     * 公告内容
     */
    private String content;


    private String remark;

    /**
     * 公告开始时间
     */
    @Column(name = "start_time")
    private Date startTime;

    /**
     * 公告结束时间
     */
    @Column(name = "end_time")
    private Date endTime;

    /**
     * 有效
     */
    private Boolean valid;

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
     * 获取公告标题
     *
     * @return title - 公告标题
     */
    public String getTitle() {
        return title;
    }

    /**
     * 设置公告标题
     *
     * @param title 公告标题
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 获取公告类型
     * 0:新功能 Features
     * 1:日常通知
     *
     * @return type - 公告类型
     * 0:新功能 Features
     * 1:日常通知
     */
    public Integer getType() {
        return type;
    }

    /**
     * 设置公告类型
     * 0:新功能 Features
     * 1:日常通知
     *
     * @param type 公告类型
     *             0:新功能 Features
     *             1:日常通知
     */
    public void setType(Integer type) {
        this.type = type;
    }

    /**
     * @return remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * @param remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取公告开始时间
     *
     * @return start_time - 公告开始时间
     */
    public Date getStartTime() {
        return startTime;
    }

    /**
     * 设置公告开始时间
     *
     * @param startTime 公告开始时间
     */
    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取公告结束时间
     *
     * @return end_time - 公告结束时间
     */
    public Date getEndTime() {
        return endTime;
    }

    /**
     * 设置公告结束时间
     *
     * @param endTime 公告结束时间
     */
    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取有效
     *
     * @return valid - 有效
     */
    public Boolean getValid() {
        return valid;
    }

    /**
     * 设置有效
     *
     * @param valid 有效
     */
    public void setValid(Boolean valid) {
        this.valid = valid;
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
     * 获取公告内容
     *
     * @return content - 公告内容
     */
    public String getContent() {
        return content;
    }

    /**
     * 设置公告内容
     *
     * @param content 公告内容
     */
    public void setContent(String content) {
        this.content = content;
    }
}