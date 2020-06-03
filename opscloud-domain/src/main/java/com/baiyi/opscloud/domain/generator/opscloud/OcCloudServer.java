package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_server")
public class OcCloudServer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "server_name")
    private String serverName;

    @Column(name = "instance_id")
    private String instanceId;

    @Column(name = "instance_name")
    private String instanceName;

    @Column(name = "region_id")
    private String regionId;

    private String zone;

    @Column(name = "cloud_server_type")
    private Integer cloudServerType;

    /**
     * 私有IP
     */
    @Column(name = "private_ip")
    private String privateIp;

    /**
     * 公网IP
     */
    @Column(name = "public_ip")
    private String publicIp;

    private Integer cpu;

    private Integer memory;

    @Column(name = "vpc_id")
    private String vpcId;

    @Column(name = "instance_type")
    private String instanceType;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "system_disk_size")
    private Integer systemDiskSize;

    @Column(name = "data_disk_size")
    private Integer dataDiskSize;

    @Column(name = "created_time")
    private Date createdTime;

    @Column(name = "expired_time")
    private Date expiredTime;

    @Column(name = "charge_type")
    private String chargeType;

    @Column(name = "server_status")
    private Integer serverStatus;

    /**
     * 续费设置
     */
    @Column(name = "renewal_status")
    private String renewalStatus;

    @Column(name = "server_id")
    private Integer serverId;

    /**
     * 允许电源管理
     */
    @Column(name = "power_mgmt")
    private Boolean powerMgmt;

    /**
     * 电源状态
     */
    @Column(name = "power_status")
    private Integer powerStatus;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;

    /**
     * instance序列化对象
     */
    @Column(name = "instance_detail")
    private String instanceDetail;

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
     * @return server_name
     */
    public String getServerName() {
        return serverName;
    }

    /**
     * @param serverName
     */
    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    /**
     * @return instance_id
     */
    public String getInstanceId() {
        return instanceId;
    }

    /**
     * @param instanceId
     */
    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    /**
     * @return instance_name
     */
    public String getInstanceName() {
        return instanceName;
    }

    /**
     * @param instanceName
     */
    public void setInstanceName(String instanceName) {
        this.instanceName = instanceName;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * @return zone
     */
    public String getZone() {
        return zone;
    }

    /**
     * @param zone
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * @return cloudserver_type
     */
    public Integer getCloudServerType() {
        return cloudServerType;
    }

    /**
     * @param cloudServerType
     */
    public void setCloudServerType(Integer cloudServerType) {
        this.cloudServerType = cloudServerType;
    }

    /**
     * 获取私有IP
     *
     * @return private_ip - 私有IP
     */
    public String getPrivateIp() {
        return privateIp;
    }

    /**
     * 设置私有IP
     *
     * @param privateIp 私有IP
     */
    public void setPrivateIp(String privateIp) {
        this.privateIp = privateIp;
    }

    /**
     * 获取公网IP
     *
     * @return public_ip - 公网IP
     */
    public String getPublicIp() {
        return publicIp;
    }

    /**
     * 设置公网IP
     *
     * @param publicIp 公网IP
     */
    public void setPublicIp(String publicIp) {
        this.publicIp = publicIp;
    }

    /**
     * @return cpu
     */
    public Integer getCpu() {
        return cpu;
    }

    /**
     * @param cpu
     */
    public void setCpu(Integer cpu) {
        this.cpu = cpu;
    }

    /**
     * @return memory
     */
    public Integer getMemory() {
        return memory;
    }

    /**
     * @param memory
     */
    public void setMemory(Integer memory) {
        this.memory = memory;
    }

    /**
     * @return vpc_id
     */
    public String getVpcId() {
        return vpcId;
    }

    /**
     * @param vpcId
     */
    public void setVpcId(String vpcId) {
        this.vpcId = vpcId;
    }

    /**
     * @return instance_type
     */
    public String getInstanceType() {
        return instanceType;
    }

    /**
     * @param instanceType
     */
    public void setInstanceType(String instanceType) {
        this.instanceType = instanceType;
    }

    /**
     * @return image_id
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * @param imageId
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * @return system_disk_size
     */
    public Integer getSystemDiskSize() {
        return systemDiskSize;
    }

    /**
     * @param systemDiskSize
     */
    public void setSystemDiskSize(Integer systemDiskSize) {
        this.systemDiskSize = systemDiskSize;
    }

    /**
     * @return data_disk_size
     */
    public Integer getDataDiskSize() {
        return dataDiskSize;
    }

    /**
     * @param dataDiskSize
     */
    public void setDataDiskSize(Integer dataDiskSize) {
        this.dataDiskSize = dataDiskSize;
    }

    /**
     * @return created_time
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * @param createdTime
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * @return expired_time
     */
    public Date getExpiredTime() {
        return expiredTime;
    }

    /**
     * @param expiredTime
     */
    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     * @return charge_type
     */
    public String getChargeType() {
        return chargeType;
    }

    /**
     * @param chargeType
     */
    public void setChargeType(String chargeType) {
        this.chargeType = chargeType;
    }

    /**
     * @return server_status
     */
    public Integer getServerStatus() {
        return serverStatus;
    }

    /**
     * @param serverStatus
     */
    public void setServerStatus(Integer serverStatus) {
        this.serverStatus = serverStatus;
    }

    /**
     * 获取续费设置
     *
     * @return renewal_status - 续费设置
     */
    public String getRenewalStatus() {
        return renewalStatus;
    }

    /**
     * 设置续费设置
     *
     * @param renewalStatus 续费设置
     */
    public void setRenewalStatus(String renewalStatus) {
        this.renewalStatus = renewalStatus;
    }

    /**
     * @return server_id
     */
    public Integer getServerId() {
        return serverId;
    }

    /**
     * @param serverId
     */
    public void setServerId(Integer serverId) {
        this.serverId = serverId;
    }

    /**
     * 获取允许电源管理
     *
     * @return power_mgmt - 允许电源管理
     */
    public Boolean getPowerMgmt() {
        return powerMgmt;
    }

    /**
     * 设置允许电源管理
     *
     * @param powerMgmt 允许电源管理
     */
    public void setPowerMgmt(Boolean powerMgmt) {
        this.powerMgmt = powerMgmt;
    }

    public Integer getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(Integer powerStatus) {
        this.powerStatus = powerStatus;
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

    /**
     * 获取instance序列化对象
     *
     * @return instance_detail - instance序列化对象
     */
    public String getInstanceDetail() {
        return instanceDetail;
    }

    /**
     * 设置instance序列化对象
     *
     * @param instanceDetail instance序列化对象
     */
    public void setInstanceDetail(String instanceDetail) {
        this.instanceDetail = instanceDetail;
    }
}