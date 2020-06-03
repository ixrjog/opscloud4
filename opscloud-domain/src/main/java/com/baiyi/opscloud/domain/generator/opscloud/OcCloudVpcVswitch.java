package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_vpc_vswitch")
public class OcCloudVpcVswitch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "zone_id")
    private String zoneId;

    /**
     * vpc id
     */
    @Column(name = "vpc_id")
    private String vpcId;

    @Column(name = "vswitch_name")
    private String vswitchName;

    /**
     * vswitch
     */
    @Column(name = "vswitch_id")
    private String vswitchId;

    /**
     * 状态
     */
    @Column(name = "vswitch_status")
    private String vswitchStatus;

    /**
     * Classless Inter-Domain Routing网段
     */
    @Column(name = "cidr_block")
    private String cidrBlock;

    @Column(name = "cloud_type")
    private Integer cloudType;

    /**
     * 说明
     */
    private String description;

    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "available_ip_address_count")
    private Integer availableIpAddressCount;

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Integer isActive;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;

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
     * @return zone_id
     */
    public String getZoneId() {
        return zoneId;
    }

    /**
     * @param zoneId
     */
    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    /**
     * 获取vpc id
     *
     * @return vpc_id - vpc id
     */
    public String getVpcId() {
        return vpcId;
    }

    /**
     * 设置vpc id
     *
     * @param vpcId vpc id
     */
    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    /**
     * @return vswitch_name
     */
    public String getVswitchName() {
        return vswitchName;
    }

    /**
     * @param vswitchName
     */
    public void setVswitchName(String vswitchName) {
        this.vswitchName = vswitchName;
    }

    /**
     * 获取vswitch
     *
     * @return vswitch_id - vswitch
     */
    public String getVswitchId() {
        return vswitchId;
    }

    /**
     * 设置vswitch
     *
     * @param vswitchId vswitch
     */
    public void setVswitchId(String vswitchId) {
        this.vswitchId = vswitchId;
    }

    /**
     * 获取状态
     *
     * @return vswitch_status - 状态
     */
    public String getVswitchStatus() {
        return vswitchStatus;
    }

    /**
     * 设置状态
     *
     * @param vswitchStatus 状态
     */
    public void setVswitchStatus(String vswitchStatus) {
        this.vswitchStatus = vswitchStatus;
    }

    /**
     * 获取Classless Inter-Domain Routing网段
     *
     * @return cidr_block - Classless Inter-Domain Routing网段
     */
    public String getCidrBlock() {
        return cidrBlock;
    }

    /**
     * 设置Classless Inter-Domain Routing网段
     *
     * @param cidrBlock Classless Inter-Domain Routing网段
     */
    public void setCidrBlock(String cidrBlock) {
        this.cidrBlock = cidrBlock;
    }

    /**
     * @return cloud_type
     */
    public Integer getCloudType() {
        return cloudType;
    }

    /**
     * @param cloudType
     */
    public void setCloudType(Integer cloudType) {
        this.cloudType = cloudType;
    }

    /**
     * 获取说明
     *
     * @return description - 说明
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置说明
     *
     * @param description 说明
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return creation_time
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * @param creationTime
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return available_ip_address_count
     */
    public Integer getAvailableIpAddressCount() {
        return availableIpAddressCount;
    }

    /**
     * @param availableIpAddressCount
     */
    public void setAvailableIpAddressCount(Integer availableIpAddressCount) {
        this.availableIpAddressCount = availableIpAddressCount;
    }

    /**
     * 获取有效
     *
     * @return is_active - 有效
     */
    public Integer getIsActive() {
        return isActive;
    }

    /**
     * 设置有效
     *
     * @param isActive 有效
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
}