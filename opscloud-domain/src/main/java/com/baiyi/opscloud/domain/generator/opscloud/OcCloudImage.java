package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_image")
public class OcCloudImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String uid;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "region_id")
    private String regionId;

    /**
     * 镜像id
     */
    @Column(name = "image_id")
    private String imageId;

    /**
     * 镜像名称
     */
    @Column(name = "image_name")
    private String imageName;

    /**
     * 镜像说明
     */
    @Column(name = "image_size")
    private Integer imageSize;

    @Column(name = "cloud_type")
    private Integer cloudType;

    /**
     * 镜像来源。
     */
    @Column(name = "image_owner_alias")
    private String imageOwnerAlias;

    @Column(name = "is_supportIo_optimized")
    private Integer isSupportioOptimized;

    @Column(name = "is_support_cloudinit")
    private Integer isSupportCloudinit;

    @Column(name = "os_name")
    private String osName;

    @Column(name = "os_name_en")
    private String osNameEn;

    private String architecture;

    @Column(name = "image_status")
    private String imageStatus;

    /**
     * 镜像创建时间
     */
    @Column(name = "creation_time")
    private Date creationTime;

    @Column(name = "os_type")
    private String osType;

    /**
     * 平台
     */
    private String platform;

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
     * instance序列化对象
     */
    @Column(name = "image_detail")
    private String imageDetail;

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
     * 获取镜像id
     *
     * @return image_id - 镜像id
     */
    public String getImageId() {
        return imageId;
    }

    /**
     * 设置镜像id
     *
     * @param imageId 镜像id
     */
    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    /**
     * 获取镜像名称
     *
     * @return image_name - 镜像名称
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * 设置镜像名称
     *
     * @param imageName 镜像名称
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * 获取镜像说明
     *
     * @return image_size - 镜像说明
     */
    public Integer getImageSize() {
        return imageSize;
    }

    /**
     * 设置镜像说明
     *
     * @param imageSize 镜像说明
     */
    public void setImageSize(Integer imageSize) {
        this.imageSize = imageSize;
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
     * 获取镜像来源。
     *
     * @return image_owner_alias - 镜像来源。
     */
    public String getImageOwnerAlias() {
        return imageOwnerAlias;
    }

    /**
     * 设置镜像来源。
     *
     * @param imageOwnerAlias 镜像来源。
     */
    public void setImageOwnerAlias(String imageOwnerAlias) {
        this.imageOwnerAlias = imageOwnerAlias;
    }

    /**
     * @return is_supportIo_optimized
     */
    public Integer getIsSupportioOptimized() {
        return isSupportioOptimized;
    }

    /**
     * @param isSupportioOptimized
     */
    public void setIsSupportioOptimized(Integer isSupportioOptimized) {
        this.isSupportioOptimized = isSupportioOptimized;
    }

    /**
     * @return is_support_cloudinit
     */
    public Integer getIsSupportCloudinit() {
        return isSupportCloudinit;
    }

    /**
     * @param isSupportCloudinit
     */
    public void setIsSupportCloudinit(Integer isSupportCloudinit) {
        this.isSupportCloudinit = isSupportCloudinit;
    }

    /**
     * @return os_name
     */
    public String getOsName() {
        return osName;
    }

    /**
     * @param osName
     */
    public void setOsName(String osName) {
        this.osName = osName;
    }

    /**
     * @return os_name_en
     */
    public String getOsNameEn() {
        return osNameEn;
    }

    /**
     * @param osNameEn
     */
    public void setOsNameEn(String osNameEn) {
        this.osNameEn = osNameEn;
    }

    /**
     * @return architecture
     */
    public String getArchitecture() {
        return architecture;
    }

    /**
     * @param architecture
     */
    public void setArchitecture(String architecture) {
        this.architecture = architecture;
    }

    /**
     * @return image_status
     */
    public String getImageStatus() {
        return imageStatus;
    }

    /**
     * @param imageStatus
     */
    public void setImageStatus(String imageStatus) {
        this.imageStatus = imageStatus;
    }

    /**
     * 获取镜像创建时间
     *
     * @return creation_time - 镜像创建时间
     */
    public Date getCreationTime() {
        return creationTime;
    }

    /**
     * 设置镜像创建时间
     *
     * @param creationTime 镜像创建时间
     */
    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    /**
     * @return os_type
     */
    public String getOsType() {
        return osType;
    }

    /**
     * @param osType
     */
    public void setOsType(String osType) {
        this.osType = osType;
    }

    /**
     * 获取平台
     *
     * @return platform - 平台
     */
    public String getPlatform() {
        return platform;
    }

    /**
     * 设置平台
     *
     * @param platform 平台
     */
    public void setPlatform(String platform) {
        this.platform = platform;
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

    /**
     * 获取instance序列化对象
     *
     * @return image_detail - instance序列化对象
     */
    public String getImageDetail() {
        return imageDetail;
    }

    /**
     * 设置instance序列化对象
     *
     * @param imageDetail instance序列化对象
     */
    public void setImageDetail(String imageDetail) {
        this.imageDetail = imageDetail;
    }
}