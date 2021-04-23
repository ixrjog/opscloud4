package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;

@Table(name = "oc_aliyun_slb")
public class OcAliyunSlb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 负载均衡实例ID
     */
    @Column(name = "load_balancer_id")
    private String loadBalancerId;

    /**
     * 负载均衡实例的名称
     */
    @Column(name = "load_balancer_name")
    private String loadBalancerName;

    /**
     * 负载均衡实例状态：
     * inactive: 此状态的实例监听不会再转发流量。
     * active: 实例创建后，默认状态为active。
     * locked: 实例已经被锁定。
     */
    @Column(name = "load_balancer_status")
    private String loadBalancerStatus;

    /**
     * 负载均衡实例服务地址
     */
    private String address;

    /**
     * 负载均衡实例的网络类型
     */
    @Column(name = "address_type")
    private String addressType;

    @Column(name = "region_id")
    private String regionId;

    /**
     * 负载均衡实例的地域名称
     */
    @Column(name = "region_id_alias")
    private String regionIdAlias;

    /**
     * 私网负载均衡实例的交换机ID
     */
    @Column(name = "v_switch_id")
    private String vSwitchId;

    /**
     * 私网负载均衡实例的专有网络ID
     */
    @Column(name = "vpc_id")
    private String vpcId;

    /**
     * 私网负载均衡实例的网络类型，取值：
     * vpc：专有网络实例。
     * classic：经典网络实例。
     */
    @Column(name = "network_type")
    private String networkType;

    /**
     * 实例的主可用区ID
     */
    @Column(name = "master_zone_id")
    private String masterZoneId;

    /**
     * 实例的备可用区ID
     */
    @Column(name = "slave_zone_id")
    private String slaveZoneId;

    /**
     * 公网实例的计费方式。取值：
     * 3：paybybandwidth，按带宽计费。
     * 4：paybytraffic，按流量计费（默认值）
     */
    @Column(name = "internet_charge_type")
    private String internetChargeType;

    /**
     * 负载均衡实例创建时间
     */
    @Column(name = "create_time")
    private String createTime;

    /**
     * 负载均衡实例付费类型，取值PayOnDemand或者PrePay
     */
    @Column(name = "pay_type")
    private String payType;

    @Column(name = "resource_group_id")
    private String resourceGroupId;

    /**
     * IP版本，可以设置为ipv4或者ipv6
     */
    @Column(name = "address_ip_version")
    private String addressIpVersion;

    /**
     * 是否关联nginx配置
     * 0 不关联
     * 1 关联
     */
    @Column(name = "is_link_nginx")
    private Integer isLinkNginx;

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
     * 获取负载均衡实例ID
     *
     * @return load_balancer_id - 负载均衡实例ID
     */
    public String getLoadBalancerId() {
        return loadBalancerId;
    }

    /**
     * 设置负载均衡实例ID
     *
     * @param loadBalancerId 负载均衡实例ID
     */
    public void setLoadBalancerId(String loadBalancerId) {
        this.loadBalancerId = loadBalancerId;
    }

    /**
     * 获取负载均衡实例的名称
     *
     * @return load_balancer_name - 负载均衡实例的名称
     */
    public String getLoadBalancerName() {
        return loadBalancerName;
    }

    /**
     * 设置负载均衡实例的名称
     *
     * @param loadBalancerName 负载均衡实例的名称
     */
    public void setLoadBalancerName(String loadBalancerName) {
        this.loadBalancerName = loadBalancerName;
    }

    /**
     * 获取负载均衡实例状态：
     * inactive: 此状态的实例监听不会再转发流量。
     * active: 实例创建后，默认状态为active。
     * locked: 实例已经被锁定。
     *
     * @return load_balancer_status - 负载均衡实例状态：
     * inactive: 此状态的实例监听不会再转发流量。
     * active: 实例创建后，默认状态为active。
     * locked: 实例已经被锁定。
     */
    public String getLoadBalancerStatus() {
        return loadBalancerStatus;
    }

    /**
     * 设置负载均衡实例状态：
     * inactive: 此状态的实例监听不会再转发流量。
     * active: 实例创建后，默认状态为active。
     * locked: 实例已经被锁定。
     *
     * @param loadBalancerStatus 负载均衡实例状态：
     *                           inactive: 此状态的实例监听不会再转发流量。
     *                           active: 实例创建后，默认状态为active。
     *                           locked: 实例已经被锁定。
     */
    public void setLoadBalancerStatus(String loadBalancerStatus) {
        this.loadBalancerStatus = loadBalancerStatus;
    }

    /**
     * 获取负载均衡实例服务地址
     *
     * @return address - 负载均衡实例服务地址
     */
    public String getAddress() {
        return address;
    }

    /**
     * 设置负载均衡实例服务地址
     *
     * @param address 负载均衡实例服务地址
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 获取负载均衡实例的网络类型
     *
     * @return address_type - 负载均衡实例的网络类型
     */
    public String getAddressType() {
        return addressType;
    }

    /**
     * 设置负载均衡实例的网络类型
     *
     * @param addressType 负载均衡实例的网络类型
     */
    public void setAddressType(String addressType) {
        this.addressType = addressType;
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
     * 获取负载均衡实例的地域名称
     *
     * @return region_id_alias - 负载均衡实例的地域名称
     */
    public String getRegionIdAlias() {
        return regionIdAlias;
    }

    /**
     * 设置负载均衡实例的地域名称
     *
     * @param regionIdAlias 负载均衡实例的地域名称
     */
    public void setRegionIdAlias(String regionIdAlias) {
        this.regionIdAlias = regionIdAlias;
    }

    /**
     * 获取私网负载均衡实例的交换机ID
     *
     * @return v_switch_id - 私网负载均衡实例的交换机ID
     */
    public String getvSwitchId() {
        return vSwitchId;
    }

    /**
     * 设置私网负载均衡实例的交换机ID
     *
     * @param vSwitchId 私网负载均衡实例的交换机ID
     */
    public void setvSwitchId(String vSwitchId) {
        this.vSwitchId = vSwitchId;
    }

    /**
     * 获取私网负载均衡实例的专有网络ID
     *
     * @return vpc_id - 私网负载均衡实例的专有网络ID
     */
    public String getVpcId() {
        return vpcId;
    }

    /**
     * 设置私网负载均衡实例的专有网络ID
     *
     * @param vpcId 私网负载均衡实例的专有网络ID
     */
    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    /**
     * 获取私网负载均衡实例的网络类型，取值：
     * vpc：专有网络实例。
     * classic：经典网络实例。
     *
     * @return network_type - 私网负载均衡实例的网络类型，取值：
     * vpc：专有网络实例。
     * classic：经典网络实例。
     */
    public String getNetworkType() {
        return networkType;
    }

    /**
     * 设置私网负载均衡实例的网络类型，取值：
     * vpc：专有网络实例。
     * classic：经典网络实例。
     *
     * @param networkType 私网负载均衡实例的网络类型，取值：
     *                    vpc：专有网络实例。
     *                    classic：经典网络实例。
     */
    public void setNetworkType(String networkType) {
        this.networkType = networkType;
    }

    /**
     * 获取实例的主可用区ID
     *
     * @return master_zone_id - 实例的主可用区ID
     */
    public String getMasterZoneId() {
        return masterZoneId;
    }

    /**
     * 设置实例的主可用区ID
     *
     * @param masterZoneId 实例的主可用区ID
     */
    public void setMasterZoneId(String masterZoneId) {
        this.masterZoneId = masterZoneId;
    }

    /**
     * 获取实例的备可用区ID
     *
     * @return slave_zone_id - 实例的备可用区ID
     */
    public String getSlaveZoneId() {
        return slaveZoneId;
    }

    /**
     * 设置实例的备可用区ID
     *
     * @param slaveZoneId 实例的备可用区ID
     */
    public void setSlaveZoneId(String slaveZoneId) {
        this.slaveZoneId = slaveZoneId;
    }

    /**
     * 获取公网实例的计费方式。取值：
     * 3：paybybandwidth，按带宽计费。
     * 4：paybytraffic，按流量计费（默认值）
     *
     * @return internet_charge_type - 公网实例的计费方式。取值：
     * 3：paybybandwidth，按带宽计费。
     * 4：paybytraffic，按流量计费（默认值）
     */
    public String getInternetChargeType() {
        return internetChargeType;
    }

    /**
     * 设置公网实例的计费方式。取值：
     * 3：paybybandwidth，按带宽计费。
     * 4：paybytraffic，按流量计费（默认值）
     *
     * @param internetChargeType 公网实例的计费方式。取值：
     *                           3：paybybandwidth，按带宽计费。
     *                           4：paybytraffic，按流量计费（默认值）
     */
    public void setInternetChargeType(String internetChargeType) {
        this.internetChargeType = internetChargeType;
    }

    /**
     * 获取负载均衡实例创建时间
     *
     * @return create_time - 负载均衡实例创建时间
     */
    public String getCreateTime() {
        return createTime;
    }

    /**
     * 设置负载均衡实例创建时间
     *
     * @param createTime 负载均衡实例创建时间
     */
    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取负载均衡实例付费类型，取值PayOnDemand或者PrePay
     *
     * @return pay_type - 负载均衡实例付费类型，取值PayOnDemand或者PrePay
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 设置负载均衡实例付费类型，取值PayOnDemand或者PrePay
     *
     * @param payType 负载均衡实例付费类型，取值PayOnDemand或者PrePay
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * @return resource_group_id
     */
    public String getResourceGroupId() {
        return resourceGroupId;
    }

    /**
     * @param resourceGroupId
     */
    public void setResourceGroupId(String resourceGroupId) {
        this.resourceGroupId = resourceGroupId;
    }

    /**
     * 获取IP版本，可以设置为ipv4或者ipv6
     *
     * @return address_ip_version - IP版本，可以设置为ipv4或者ipv6
     */
    public String getAddressIpVersion() {
        return addressIpVersion;
    }

    /**
     * 设置IP版本，可以设置为ipv4或者ipv6
     *
     * @param addressIpVersion IP版本，可以设置为ipv4或者ipv6
     */
    public void setAddressIpVersion(String addressIpVersion) {
        this.addressIpVersion = addressIpVersion;
    }

    /**
     * 获取是否关联nginx配置
     * 0 不关联
     * 1 关联
     *
     * @return is_link_nginx - 是否关联nginx配置
     * 0 不关联
     * 1 关联
     */
    public Integer getIsLinkNginx() {
        return isLinkNginx;
    }

    /**
     * 设置是否关联nginx配置
     * 0 不关联
     * 1 关联
     *
     * @param isLinkNginx 是否关联nginx配置
     *                    0 不关联
     *                    1 关联
     */
    public void setIsLinkNginx(Integer isLinkNginx) {
        this.isLinkNginx = isLinkNginx;
    }
}