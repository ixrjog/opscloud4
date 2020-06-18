package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_workorder_ticket_entry")
public class OcWorkorderTicketEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 工单票据id
     */
    @Column(name = "workorder_ticket_id")
    private Integer workorderTicketId;

    /**
     * 名称
     */
    private String name;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 状态
     */
    @Column(name = "entry_status")
    private Integer entryStatus;

    /**
     * 说明
     */
    private String comment;

    /**
     * 条目key
     */
    @Column(name = "entry_key")
    private String entryKey;

    @Column(name = "entry_result")
    private String entryResult;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 条目详情
     */
    @Column(name = "entry_detail")
    private String entryDetail;

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
     * 获取工单票据id
     *
     * @return workorder_ticket_id - 工单票据id
     */
    public Integer getWorkorderTicketId() {
        return workorderTicketId;
    }

    /**
     * 设置工单票据id
     *
     * @param workorderTicketId 工单票据id
     */
    public void setWorkorderTicketId(Integer workorderTicketId) {
        this.workorderTicketId = workorderTicketId;
    }

    /**
     * 获取名称
     *
     * @return name - 名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置名称
     *
     * @param name 名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取业务id
     *
     * @return business_id - 业务id
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * 设置业务id
     *
     * @param businessId 业务id
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * 获取状态
     *
     * @return entry_status - 状态
     */
    public Integer getEntryStatus() {
        return entryStatus;
    }

    /**
     * 设置状态
     *
     * @param entryStatus 状态
     */
    public void setEntryStatus(Integer entryStatus) {
        this.entryStatus = entryStatus;
    }

    /**
     * 获取说明
     *
     * @return comment - 说明
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置说明
     *
     * @param comment 说明
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * 获取条目key
     *
     * @return entry_key - 条目key
     */
    public String getEntryKey() {
        return entryKey;
    }

    /**
     * 设置条目key
     *
     * @param entryKey 条目key
     */
    public void setEntryKey(String entryKey) {
        this.entryKey = entryKey;
    }

    /**
     * @return entry_result
     */
    public String getEntryResult() {
        return entryResult;
    }

    /**
     * @param entryResult
     */
    public void setEntryResult(String entryResult) {
        this.entryResult = entryResult;
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
     * 获取条目详情
     *
     * @return entry_detail - 条目详情
     */
    public String getEntryDetail() {
        return entryDetail;
    }

    /**
     * 设置条目详情
     *
     * @param entryDetail 条目详情
     */
    public void setEntryDetail(String entryDetail) {
        this.entryDetail = entryDetail;
    }
}