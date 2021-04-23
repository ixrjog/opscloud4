package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_domain_record")
public class OcAliyunDomainRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 域名名称
     */
    @Column(name = "domain_name")
    private String domainName;

    /**
     * 解析记录ID
     */
    @Column(name = "record_id")
    private String recordId;

    /**
     * 主机记录
     */
    @Column(name = "record_rr")
    private String recordRr;

    /**
     * 记录类型
     */
    @Column(name = "record_type")
    private String recordType;

    @Column(name = "record_value")
    private String recordValue;

    @Column(name = "record_ttl")
    private Long recordTtl;

    @Column(name = "record_priority")
    private Long recordPriority;

    /**
     * 解析线路
     */
    @Column(name = "record_line")
    private String recordLine;

    /**
     * 解析记录状态
     */
    @Column(name = "record_status")
    private String recordStatus;

    /**
     * 解析记录锁定状态
     */
    @Column(name = "record_locked")
    private String recordLocked;

    /**
     * 负载均衡权重
     */
    @Column(name = "record_weight")
    private Integer recordWeight;

    private String remark;

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
     * 获取域名名称
     *
     * @return domain_name - 域名名称
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * 设置域名名称
     *
     * @param domainName 域名名称
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * 获取解析记录ID
     *
     * @return record_id - 解析记录ID
     */
    public String getRecordId() {
        return recordId;
    }

    /**
     * 设置解析记录ID
     *
     * @param recordId 解析记录ID
     */
    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    /**
     * 获取主机记录
     *
     * @return record_rr - 主机记录
     */
    public String getRecordRr() {
        return recordRr;
    }

    /**
     * 设置主机记录
     *
     * @param recordRr 主机记录
     */
    public void setRecordRr(String recordRr) {
        this.recordRr = recordRr;
    }

    /**
     * 获取记录类型
     *
     * @return record_type - 记录类型
     */
    public String getRecordType() {
        return recordType;
    }

    /**
     * 设置记录类型
     *
     * @param recordType 记录类型
     */
    public void setRecordType(String recordType) {
        this.recordType = recordType;
    }

    /**
     * @return record_value
     */
    public String getRecordValue() {
        return recordValue;
    }

    /**
     * @param recordValue
     */
    public void setRecordValue(String recordValue) {
        this.recordValue = recordValue;
    }

    /**
     * @return record_ttl
     */
    public Long getRecordTtl() {
        return recordTtl;
    }

    /**
     * @param recordTtl
     */
    public void setRecordTtl(Long recordTtl) {
        this.recordTtl = recordTtl;
    }

    /**
     * @return record_priority
     */
    public Long getRecordPriority() {
        return recordPriority;
    }

    /**
     * @param recordPriority
     */
    public void setRecordPriority(Long recordPriority) {
        this.recordPriority = recordPriority;
    }

    /**
     * 获取解析线路
     *
     * @return record_line - 解析线路
     */
    public String getRecordLine() {
        return recordLine;
    }

    /**
     * 设置解析线路
     *
     * @param recordLine 解析线路
     */
    public void setRecordLine(String recordLine) {
        this.recordLine = recordLine;
    }

    /**
     * 获取解析记录状态
     *
     * @return record_status - 解析记录状态
     */
    public String getRecordStatus() {
        return recordStatus;
    }

    /**
     * 设置解析记录状态
     *
     * @param recordStatus 解析记录状态
     */
    public void setRecordStatus(String recordStatus) {
        this.recordStatus = recordStatus;
    }

    /**
     * 获取解析记录锁定状态
     *
     * @return record_locked - 解析记录锁定状态
     */
    public String getRecordLocked() {
        return recordLocked;
    }

    /**
     * 设置解析记录锁定状态
     *
     * @param recordLocked 解析记录锁定状态
     */
    public void setRecordLocked(String recordLocked) {
        this.recordLocked = recordLocked;
    }

    /**
     * 获取负载均衡权重
     *
     * @return record_weight - 负载均衡权重
     */
    public Integer getRecordWeight() {
        return recordWeight;
    }

    /**
     * 设置负载均衡权重
     *
     * @param recordWeight 负载均衡权重
     */
    public void setRecordWeight(Integer recordWeight) {
        this.recordWeight = recordWeight;
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