package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_domain")
public class OcAliyunDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 域名
     */
    @Column(name = "domain_name")
    private String domainName;

    /**
     * 实例编号
     */
    @Column(name = "instance_id")
    private String instanceId;

    /**
     * 域名实名认证状态。取值：
     * FAILED：实名认证失败。
     * SUCCEED：实名认证成功。
     * NONAUDIT：未实名认证。
     * AUDITING：审核中
     */
    @Column(name = "domain_audit_status")
    private String domainAuditStatus;

    /**
     * 域名分组编号
     */
    @Column(name = "domain_group_id")
    private String domainGroupId;

    /**
     * 域名分组名称
     */
    @Column(name = "domain_group_name")
    private String domainGroupName;

    /**
     * 域名状态。取值：\n1：急需续费。\n 2：急需赎回。\n 3：正常。
     */
    @Column(name = "domain_status")
    private String domainStatus;

    /**
     * 域名类型。取值：
     * New gTLD。
     * gTLD。
     * ccTLD。
     */
    @Column(name = "domain_type")
    private String domainType;

    /**
     * 域名到期日期
     */
    @Column(name = "expiration_date")
    private String expirationDate;

    /**
     * 产品ID
     */
    @Column(name = "product_id")
    private String productId;

    /**
     * 域名注册类型。取值：\n1：个人。\n2：企业。
     */
    @Column(name = "registrant_type")
    private String registrantType;

    /**
     * 注册时间
     */
    @Column(name = "registration_date")
    private String registrationDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 0无效，1有效
     */
    @Column(name = "is_active")
    private Integer isActive;

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
     * 获取域名
     *
     * @return domain_name - 域名
     */
    public String getDomainName() {
        return domainName;
    }

    /**
     * 设置域名
     *
     * @param domainName 域名
     */
    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    /**
     * 获取实例编号
     *
     * @return instance_id - 实例编号
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * 设置实例编号
     *
     * @param instanceId 实例编号
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * 获取域名实名认证状态。取值：
     * FAILED：实名认证失败。
     * SUCCEED：实名认证成功。
     * NONAUDIT：未实名认证。
     * AUDITING：审核中
     *
     * @return domain_audit_status - 域名实名认证状态。取值：
     * FAILED：实名认证失败。
     * SUCCEED：实名认证成功。
     * NONAUDIT：未实名认证。
     * AUDITING：审核中
     */
    public String getDomainAuditStatus() {
        return domainAuditStatus;
    }

    /**
     * 设置域名实名认证状态。取值：
     * FAILED：实名认证失败。
     * SUCCEED：实名认证成功。
     * NONAUDIT：未实名认证。
     * AUDITING：审核中
     *
     * @param domainAuditStatus 域名实名认证状态。取值：
     *                          FAILED：实名认证失败。
     *                          SUCCEED：实名认证成功。
     *                          NONAUDIT：未实名认证。
     *                          AUDITING：审核中
     */
    public void setDomainAuditStatus(String domainAuditStatus) {
        this.domainAuditStatus = domainAuditStatus;
    }

    /**
     * 获取域名分组编号
     *
     * @return domain_group_id - 域名分组编号
     */
    public String getDomainGroupId() {
        return domainGroupId;
    }

    /**
     * 设置域名分组编号
     *
     * @param domainGroupId 域名分组编号
     */
    public void setDomainGroupId(String domainGroupId) {
        this.domainGroupId = domainGroupId;
    }

    /**
     * 获取域名分组名称
     *
     * @return domain_group_name - 域名分组名称
     */
    public String getDomainGroupName() {
        return domainGroupName;
    }

    /**
     * 设置域名分组名称
     *
     * @param domainGroupName 域名分组名称
     */
    public void setDomainGroupName(String domainGroupName) {
        this.domainGroupName = domainGroupName;
    }

    /**
     * 获取域名状态。取值：\n1：急需续费。\n 2：急需赎回。\n 3：正常。
     *
     * @return domain_status - 域名状态。取值：\n1：急需续费。\n 2：急需赎回。\n 3：正常。
     */
    public String getDomainStatus() {
        return domainStatus;
    }

    /**
     * 设置域名状态。取值：\n1：急需续费。\n 2：急需赎回。\n 3：正常。
     *
     * @param domainStatus 域名状态。取值：\n1：急需续费。\n 2：急需赎回。\n 3：正常。
     */
    public void setDomainStatus(String domainStatus) {
        this.domainStatus = domainStatus;
    }

    /**
     * 获取域名类型。取值：
     * New gTLD。
     * gTLD。
     * ccTLD。
     *
     * @return domain_type - 域名类型。取值：
     * New gTLD。
     * gTLD。
     * ccTLD。
     */
    public String getDomainType() {
        return domainType;
    }

    /**
     * 设置域名类型。取值：
     * New gTLD。
     * gTLD。
     * ccTLD。
     *
     * @param domainType 域名类型。取值：
     *                   New gTLD。
     *                   gTLD。
     *                   ccTLD。
     */
    public void setDomainType(String domainType) {
        this.domainType = domainType;
    }

    /**
     * 获取域名到期日期
     *
     * @return expiration_date - 域名到期日期
     */
    public String getExpirationDate() {
        return expirationDate;
    }

    /**
     * 设置域名到期日期
     *
     * @param expirationDate 域名到期日期
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * 获取产品ID
     *
     * @return product_id - 产品ID
     */
    public String getProductId() {
        return productId;
    }

    /**
     * 设置产品ID
     *
     * @param productId 产品ID
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * 获取域名注册类型。取值：\n1：个人。\n2：企业。
     *
     * @return registrant_type - 域名注册类型。取值：\n1：个人。\n2：企业。
     */
    public String getRegistrantType() {
        return registrantType;
    }

    /**
     * 设置域名注册类型。取值：\n1：个人。\n2：企业。
     *
     * @param registrantType 域名注册类型。取值：\n1：个人。\n2：企业。
     */
    public void setRegistrantType(String registrantType) {
        this.registrantType = registrantType;
    }

    /**
     * 获取注册时间
     *
     * @return registration_date - 注册时间
     */
    public String getRegistrationDate() {
        return registrationDate;
    }

    /**
     * 设置注册时间
     *
     * @param registrationDate 注册时间
     */
    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取0无效，1有效
     *
     * @return is_active - 0无效，1有效
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * 设置0无效，1有效
     *
     * @param isActive 0无效，1有效
     */
    public void setIsActive(Integer isActive) {
        this.isActive = isActive;
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