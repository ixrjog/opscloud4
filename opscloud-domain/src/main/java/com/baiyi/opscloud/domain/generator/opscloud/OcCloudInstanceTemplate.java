package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_instance_template")
public class OcCloudInstanceTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cloud_type")
    private Integer cloudType;

    @Column(name = "template_name")
    private String templateName;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "image_id")
    private String imageId;

    @Column(name = "vpc_id")
    private String vpcId;

    @Column(name = "vpc_name")
    private String vpcName;

    @Column(name = "security_group_id")
    private String securityGroupId;

    @Column(name = "security_group_name")
    private String securityGroupName;

    @Column(name = "io_optimized")
    private String ioOptimized;

    /**
     * 使用次数
     */
    @Column(name = "usage_count")
    private Integer usageCount;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    private String comment;

    @Column(name = "template_yaml")
    private String templateYaml;

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
     * @return template_name
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * @param templateName
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
     * @return vpc_name
     */
    public String getVpcName() {
        return vpcName;
    }

    /**
     * @param vpcName
     */
    public void setVpcName(String vpcName) {
        this.vpcName = vpcName;
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
     * @return io_optimized
     */
    public String getIoOptimized() {
        return ioOptimized;
    }

    /**
     * @param ioOptimized
     */
    public void setIoOptimized(String ioOptimized) {
        this.ioOptimized = ioOptimized;
    }

    /**
     * 获取使用次数
     *
     * @return usage_count - 使用次数
     */
    public Integer getUsageCount() {
        return usageCount;
    }

    /**
     * 设置使用次数
     *
     * @param usageCount 使用次数
     */
    public void setUsageCount(Integer usageCount) {
        this.usageCount = usageCount;
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

    public String getTemplateYaml() {
        return templateYaml;
    }

    public void setTemplateYaml(String templateYaml) {
        this.templateYaml = templateYaml;
    }
}