package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_instance_type")
public class OcCloudInstanceType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "cloud_type")
    private Integer cloudType;

    @Column(name = "instance_type_family")
    private String instanceTypeFamily;

    @Column(name = "instance_type_id")
    private String instanceTypeId;

    @Column(name = "region_id")
    private String regionId;

    @Column(name = "cpu_core_count")
    private Integer cpuCoreCount;

    @Column(name = "memory_size")
    private Float memorySize;

    @Column(name = "instance_family_level")
    private String instanceFamilyLevel;

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
     * @return instance_type_family
     */
    public String getInstanceTypeFamily() {
        return instanceTypeFamily;
    }

    /**
     * @param instanceTypeFamily
     */
    public void setInstanceTypeFamily(String instanceTypeFamily) {
        this.instanceTypeFamily = instanceTypeFamily;
    }

    /**
     * @return instance_type_id
     */
    public String getInstanceTypeId() {
        return instanceTypeId;
    }

    /**
     * @param instanceTypeId
     */
    public void setInstanceTypeId(String instanceTypeId) {
        this.instanceTypeId = instanceTypeId;
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
     * @return cpu_core_count
     */
    public Integer getCpuCoreCount() {
        return cpuCoreCount;
    }

    /**
     * @param cpuCoreCount
     */
    public void setCpuCoreCount(Integer cpuCoreCount) {
        this.cpuCoreCount = cpuCoreCount;
    }

    /**
     * @return memory_size
     */
    public Float getMemorySize() {
        return memorySize;
    }

    /**
     * @param memorySize
     */
    public void setMemorySize(Float memorySize) {
        this.memorySize = memorySize;
    }

    /**
     * @return instance_family_level
     */
    public String getInstanceFamilyLevel() {
        return instanceFamilyLevel;
    }

    /**
     * @param instanceFamilyLevel
     */
    public void setInstanceFamilyLevel(String instanceFamilyLevel) {
        this.instanceFamilyLevel = instanceFamilyLevel;
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