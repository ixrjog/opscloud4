package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_vpc_security_group")
public class OcCloudVpcSecurityGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "region_id")
    private String regionId;

    /**
     * vpc id
     */
    @Column(name = "vpc_id")
    private String vpcId;

    @Column(name = "security_group_name")
    private String securityGroupName;

    @Column(name = "security_group_id")
    private String securityGroupId;

    @Column(name = "security_group_type")
    private String securityGroupType;

    @Column(name = "cloud_type")
    private Integer cloudType;

    /**
     * 说明
     */
    private String description;

    @Column(name = "creation_time")
    private Date creationTime;

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
     * @return security_group_name
     */
    public String getSecurityGroupName() {
        return securityGroupName;
    }

    /**
     * @param securityGroupName
     */
    public void setSecurityGroupName(String securityGroupName) {
        this.securityGroupName = securityGroupName;
    }

    /**
     * @return security_group_id
     */
    public String getSecurityGroupId() {
        return securityGroupId;
    }

    /**
     * @param securityGroupId
     */
    public void setSecurityGroupId(String securityGroupId) {
        this.securityGroupId = securityGroupId;
    }

    /**
     * @return security_group_type
     */
    public String getSecurityGroupType() {
        return securityGroupType;
    }

    /**
     * @param securityGroupType
     */
    public void setSecurityGroupType(String securityGroupType) {
        this.securityGroupType = securityGroupType;
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