package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_vpc")
public class OcCloudVpc {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uid;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "region_id")
    private String regionId;

    /**
     * vpc id
     */
    @Column(name = "vpc_id")
    private String vpcId;

    /**
     * vpc名称
     */
    @Column(name = "vpc_name")
    private String vpcName;

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

    /**
     * 有效
     */
    @Column(name = "is_active")
    private Integer isActive;

    /**
     * 镜像被删除
     */
    @Column(name = "is_deleted")
    private Integer isDeleted;

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
     * @return uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * @param uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * @return account_name
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * @param accountName
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
     * 获取vpc名称
     *
     * @return vpc_name - vpc名称
     */
    public String getVpcName() {
        return vpcName;
    }

    /**
     * 设置vpc名称
     *
     * @param vpcName vpc名称
     */
    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
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
     * 获取镜像被删除
     *
     * @return is_deleted - 镜像被删除
     */
    public Integer getIsDeleted() {
        return isDeleted;
    }

    /**
     * 设置镜像被删除
     *
     * @param isDeleted 镜像被删除
     */
    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
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