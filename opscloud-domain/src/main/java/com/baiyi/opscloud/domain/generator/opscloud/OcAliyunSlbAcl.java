package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_aliyun_slb_acl")
public class OcAliyunSlbAcl {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 访问控制策略组id
     */
    @Column(name = "slb_acl_id")
    private String slbAclId;

    /**
     * 访问控制策略组名称
     */
    @Column(name = "slb_acl_name")
    private String slbAclName;

    /**
     * 关联的负载均衡实例的IP地址类型
     */
    @Column(name = "address_ip_version")
    private String addressIpVersion;

    /**
     * 资源组id
     */
    @Column(name = "resource_group_id")
    private String resourceGroupId;

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
     * 获取访问控制策略组id
     *
     * @return slb_acl_id - 访问控制策略组id
     */
    public String getSlbAclId() {
        return slbAclId;
    }

    /**
     * 设置访问控制策略组id
     *
     * @param slbAclId 访问控制策略组id
     */
    public void setSlbAclId(String slbAclId) {
        this.slbAclId = slbAclId;
    }

    /**
     * 获取访问控制策略组名称
     *
     * @return slb_acl_name - 访问控制策略组名称
     */
    public String getSlbAclName() {
        return slbAclName;
    }

    /**
     * 设置访问控制策略组名称
     *
     * @param slbAclName 访问控制策略组名称
     */
    public void setSlbAclName(String slbAclName) {
        this.slbAclName = slbAclName;
    }

    /**
     * 获取关联的负载均衡实例的IP地址类型
     *
     * @return address_ip_version - 关联的负载均衡实例的IP地址类型
     */
    public String getAddressIpVersion() {
        return addressIpVersion;
    }

    /**
     * 设置关联的负载均衡实例的IP地址类型
     *
     * @param addressIpVersion 关联的负载均衡实例的IP地址类型
     */
    public void setAddressIpVersion(String addressIpVersion) {
        this.addressIpVersion = addressIpVersion;
    }

    /**
     * 获取资源组id
     *
     * @return resource_group_id - 资源组id
     */
    public String getResourceGroupId() {
        return resourceGroupId;
    }

    /**
     * 设置资源组id
     *
     * @param resourceGroupId 资源组id
     */
    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
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