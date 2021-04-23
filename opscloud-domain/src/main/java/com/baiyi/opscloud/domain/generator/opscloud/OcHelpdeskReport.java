package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_helpdesk_report")
public class OcHelpdeskReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 类型
     */
    @Column(name = "helpdesk_type")
    private Integer helpdeskType;

    /**
     * 时间
     */
    @Column(name = "helpdesk_time")
    private Date helpdeskTime;

    @Column(name = "helpdesk_cnt")
    private Integer helpdeskCnt;

    private String comment;

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
     * 获取类型
     *
     * @return helpdesk_type - 类型
     */
    public Integer getHelpdeskType() {
        return helpdeskType;
    }

    /**
     * 设置类型
     *
     * @param helpdeskType 类型
     */
    public void setHelpdeskType(Integer helpdeskType) {
        this.helpdeskType = helpdeskType;
    }

    /**
     * 获取时间
     *
     * @return helpdesk_time - 时间
     */
    public Date getHelpdeskTime() {
        return helpdeskTime;
    }

    /**
     * 设置时间
     *
     * @param helpdeskTime 时间
     */
    public void setHelpdeskTime(Date helpdeskTime) {
        this.helpdeskTime = helpdeskTime;
    }

    /**
     * @return helpdesk_cnt
     */
    public Integer getHelpdeskCnt() {
        return helpdeskCnt;
    }

    /**
     * @param helpdeskCnt
     */
    public void setHelpdeskCnt(Integer helpdeskCnt) {
        this.helpdeskCnt = helpdeskCnt;
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