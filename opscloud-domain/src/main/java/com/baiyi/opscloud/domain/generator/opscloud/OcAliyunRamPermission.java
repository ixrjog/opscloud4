package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_ram_permission")
public class OcAliyunRamPermission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 阿里云账户uid
     */
    @Column(name = "account_uid")
    private String accountUid;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private Integer userId;

    /**
     * 策略id
     */
    @Column(name = "policy_id")
    private Integer policyId;

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
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 获取策略id
     *
     * @return policy_id - 策略id
     */
    public Integer getPolicyId() {
        return policyId;
    }

    /**
     * 设置策略id
     *
     * @param policyId 策略id
     */
    public void setPolicyId(Integer policyId) {
        this.policyId = policyId;
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