package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ram_user")
public class OcAliyunRamUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 阿里云账户uid
     */
    @Column(name = "account_uid")
    private String accountUid;

    @Column(name = "ram_user_id")
    private String ramUserId;

    @Column(name = "ram_username")
    private String ramUsername;

    @Column(name = "ram_display_name")
    private String ramDisplayName;

    private String mobile;

    @Column(name = "access_keys")
    private Integer accessKeys;

    /**
     * 账户类型
     */
    @Column(name = "ram_type")
    private Integer ramType;

    private String comment;

    /**
     * 账户创建时间
     */
    @Column(name = "create_date")
    private Date createDate;

    /**
     * 账户更新时间
     */
    @Column(name = "update_date")
    private Date updateDate;

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
     * @return ram_user_id
     */
    public String getRamUserId() {
        return ramUserId;
    }

    /**
     * @param ramUserId
     */
    public void setRamUserId(String ramUserId) {
        this.ramUserId = ramUserId;
    }

    /**
     * @return ram_username
     */
    public String getRamUsername() {
        return ramUsername;
    }

    /**
     * @param ramUsername
     */
    public void setRamUsername(String ramUsername) {
        this.ramUsername = ramUsername;
    }

    /**
     * @return ram_display_name
     */
    public String getRamDisplayName() {
        return ramDisplayName;
    }

    /**
     * @param ramDisplayName
     */
    public void setRamDisplayName(String ramDisplayName) {
        this.ramDisplayName = ramDisplayName;
    }

    /**
     * @return mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * @param mobile
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * @return access_keys
     */
    public Integer getAccessKeys() {
        return accessKeys;
    }

    /**
     * @param accessKeys
     */
    public void setAccessKeys(Integer accessKeys) {
        this.accessKeys = accessKeys;
    }

    /**
     * 获取账户类型
     *
     * @return ram_type - 账户类型
     */
    public Integer getRamType() {
        return ramType;
    }

    /**
     * 设置账户类型
     *
     * @param ramType 账户类型
     */
    public void setRamType(Integer ramType) {
        this.ramType = ramType;
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
     * 获取账户创建时间
     *
     * @return create_date - 账户创建时间
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * 设置账户创建时间
     *
     * @param createDate 账户创建时间
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    /**
     * 获取账户更新时间
     *
     * @return update_date - 账户更新时间
     */
    public Date getUpdateDate() {
        return updateDate;
    }

    /**
     * 设置账户更新时间
     *
     * @param updateDate 账户更新时间
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