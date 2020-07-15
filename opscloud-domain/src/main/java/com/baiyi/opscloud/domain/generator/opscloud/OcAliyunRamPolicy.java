package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ram_policy")
public class OcAliyunRamPolicy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 阿里云账户uid
     */
    @Column(name = "account_uid")
    private String accountUid;

    /**
     * 策略名称
     */
    @Column(name = "policy_name")
    private String policyName;

    /**
     * 策略类型
     */
    @Column(name = "policy_type")
    private String policyType;

    /**
     * 策略说明
     */
    private String description;

    /**
     * 默认版本
     */
    @Column(name = "default_version")
    private String defaultVersion;

    /**
     * 引用次数
     */
    @Column(name = "attachment_count")
    private Integer attachmentCount;

    /**
     * 允许工单申请
     */
    @Column(name = "in_workorder")
    private Integer inWorkorder;

    private String comment;

    /**
     * 策略创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 策略更新时间
     */
    @Column(name = "update_date", insertable = false, updatable = false)
    private Date updateDate;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time")
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
     * 获取阿里云账户uid
     *
     * @return account_uid - 阿里云账户uid
     */
    public String getAccountUid() {
        return accountUid;
    }

    /**
     * 设置阿里云账户uid
     *
     * @param accountUid 阿里云账户uid
     */
    public void setAccountUid(String accountUid) {
        this.accountUid = accountUid;
    }

    /**
     * 获取策略名称
     *
     * @return policy_name - 策略名称
     */
    public String getPolicyName() {
        return policyName;
    }

    /**
     * 设置策略名称
     *
     * @param policyName 策略名称
     */
    public void setPolicyName(String policyName) {
        this.policyName = policyName;
    }

    /**
     * 获取策略类型
     *
     * @return policy_type - 策略类型
     */
    public String getPolicyType() {
        return policyType;
    }

    /**
     * 设置策略类型
     *
     * @param policyType 策略类型
     */
    public void setPolicyType(String policyType) {
        this.policyType = policyType;
    }

    /**
     * 获取策略说明
     *
     * @return description - 策略说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置策略说明
     *
     * @param description 策略说明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取默认版本
     *
     * @return default_version - 默认版本
     */
    public String getDefaultVersion() {
        return defaultVersion;
    }

    /**
     * 设置默认版本
     *
     * @param defaultVersion 默认版本
     */
    public void setDefaultVersion(String defaultVersion) {
        this.defaultVersion = defaultVersion;
    }

    /**
     * 获取引用次数
     *
     * @return attachment_count - 引用次数
     */
    public Integer getAttachmentCount() {
        return attachmentCount;
    }

    /**
     * 设置引用次数
     *
     * @param attachmentCount 引用次数
     */
    public void setAttachmentCount(Integer attachmentCount) {
        this.attachmentCount = attachmentCount;
    }

    /**
     * 获取允许工单申请
     *
     * @return in_workorder - 允许工单申请
     */
    public Integer getInWorkorder() {
        return inWorkorder;
    }

    /**
     * 设置允许工单申请
     *
     * @param inWorkorder 允许工单申请
     */
    public void setInWorkorder(Integer inWorkorder) {
        this.inWorkorder = inWorkorder;
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
     * 获取策略创建时间
     *
     * @return create_date - 策略创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置策略创建时间
     *
     * @param createDate 策略创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取策略更新时间
     *
     * @return update_date - 策略更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置策略更新时间
     *
     * @param updateDate 策略更新时间
     */
    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
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