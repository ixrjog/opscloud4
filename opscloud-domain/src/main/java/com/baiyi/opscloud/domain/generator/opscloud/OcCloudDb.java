package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_db")
public class OcCloudDb {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 云账户uid
     */
    private String uid;

    /**
     * 云账户名称
     */
    @Column(name = "account_name")
    private String accountName;

    /**
     * 云数据库类型
     */
    @Column(name = "cloud_db_type")
    private Integer cloudDbType;

    /**
     * 地域id
     */
    @Column(name = "region_id")
    private String regionId;

    /**
     * 实例id
     */
    @Column(name = "db_instance_id")
    private String dbInstanceId;

    /**
     * 实例描述系统获取
     */
    @Column(name = "db_instance_description")
    private String dbInstanceDescription;

    /**
     * 实例类型
     */
    @Column(name = "db_instance_type")
    private String dbInstanceType;

    /**
     * 数据库类型
     */
    private String engine;

    /**
     * 数据库版本
     */
    @Column(name = "engine_version")
    private String engineVersion;

    /**
     * 可用区id
     */
    private String zone;

    /**
     * 实例的付费类型
     */
    @Column(name = "pay_type")
    private String payType;

    /**
     * 实例状态
     */
    @Column(name = "db_instance_status")
    private String dbInstanceStatus;

    /**
     * 到期时间
     */
    @Column(name = "expired_time")
    private Date expiredTime;

    /**
     * 实例的网络类型
     */
    @Column(name = "instance_network_type")
    private String instanceNetworkType;

    /**
     * 实例的访问模式
     */
    @Column(name = "connection_mode")
    private String connectionMode;

    /**
     * 实例的网络连接类型
     */
    @Column(name = "db_instance_net_type")
    private String dbInstanceNetType;

    /**
     * 实例储存类型
     */
    @Column(name = "db_Instance_storage_type")
    private String dbInstanceStorageType;

    /**
     * 实例规格
     */
    @Column(name = "db_Instance_class")
    private String dbInstanceClass;

    /**
     * 创建时间
     */
    @Column(name = "created_time")
    private Date createdTime;

    /**
     * 实例系列
     */
    private String category;

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
     * 获取云账户uid
     *
     * @return uid - 云账户uid
     */
    public String getUid() {
        return uid;
    }

    /**
     * 设置云账户uid
     *
     * @param uid 云账户uid
     */
    public void setUid(String uid) {
        this.uid = uid;
    }

    /**
     * 获取云账户名称
     *
     * @return account_name - 云账户名称
     */
    public String getAccountName() {
        return accountName;
    }

    /**
     * 设置云账户名称
     *
     * @param accountName 云账户名称
     */
    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    /**
     * 获取云数据库类型
     *
     * @return cloud_db_type - 云数据库类型
     */
    public Integer getCloudDbType() {
        return cloudDbType;
    }

    /**
     * 设置云数据库类型
     *
     * @param cloudDbType 云数据库类型
     */
    public void setCloudDbType(Integer cloudDbType) {
        this.cloudDbType = cloudDbType;
    }

    /**
     * 获取地域id
     *
     * @return region_id - 地域id
     */
    public String getRegionId() {
        return regionId;
    }

    /**
     * 设置地域id
     *
     * @param regionId 地域id
     */
    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    /**
     * 获取实例id
     *
     * @return db_instance_id - 实例id
     */
    public String getDbInstanceId() {
        return dbInstanceId;
    }

    /**
     * 设置实例id
     *
     * @param dbInstanceId 实例id
     */
    public void setDbInstanceId(String dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
    }

    /**
     * 获取实例描述系统获取
     *
     * @return db_instance_description - 实例描述系统获取
     */
    public String getDbInstanceDescription() {
        return dbInstanceDescription;
    }

    /**
     * 设置实例描述系统获取
     *
     * @param dbInstanceDescription 实例描述系统获取
     */
    public void setDbInstanceDescription(String dbInstanceDescription) {
        this.dbInstanceDescription = dbInstanceDescription;
    }

    /**
     * 获取实例类型
     *
     * @return db_instance_type - 实例类型
     */
    public String getDbInstanceType() {
        return dbInstanceType;
    }

    /**
     * 设置实例类型
     *
     * @param dbInstanceType 实例类型
     */
    public void setDbInstanceType(String dbInstanceType) {
        this.dbInstanceType = dbInstanceType;
    }

    /**
     * 获取数据库类型
     *
     * @return engine - 数据库类型
     */
    public String getEngine() {
        return engine;
    }

    /**
     * 设置数据库类型
     *
     * @param engine 数据库类型
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * 获取数据库版本
     *
     * @return engine_version - 数据库版本
     */
    public String getEngineVersion() {
        return engineVersion;
    }

    /**
     * 设置数据库版本
     *
     * @param engineVersion 数据库版本
     */
    public void setEngineVersion(String engineVersion) {
        this.engineVersion = engineVersion;
    }

    /**
     * 获取可用区id
     *
     * @return zone - 可用区id
     */
    public String getZone() {
        return zone;
    }

    /**
     * 设置可用区id
     *
     * @param zone 可用区id
     */
    public void setZone(String zone) {
        this.zone = zone;
    }

    /**
     * 获取实例的付费类型
     *
     * @return pay_type - 实例的付费类型
     */
    public String getPayType() {
        return payType;
    }

    /**
     * 设置实例的付费类型
     *
     * @param payType 实例的付费类型
     */
    public void setPayType(String payType) {
        this.payType = payType;
    }

    /**
     * 获取实例状态
     *
     * @return db_instance_status - 实例状态
     */
    public String getDbInstanceStatus() {
        return dbInstanceStatus;
    }

    /**
     * 设置实例状态
     *
     * @param dbInstanceStatus 实例状态
     */
    public void setDbInstanceStatus(String dbInstanceStatus) {
        this.dbInstanceStatus = dbInstanceStatus;
    }

    /**
     * 获取到期时间
     *
     * @return expired_time - 到期时间
     */
    public Date getExpiredTime() {
        return expiredTime;
    }

    /**
     * 设置到期时间
     *
     * @param expiredTime 到期时间
     */
    public void setExpiredTime(Date expiredTime) {
        this.expiredTime = expiredTime;
    }

    /**
     * 获取实例的网络类型
     *
     * @return instance_network_type - 实例的网络类型
     */
    public String getInstanceNetworkType() {
        return instanceNetworkType;
    }

    /**
     * 设置实例的网络类型
     *
     * @param instanceNetworkType 实例的网络类型
     */
    public void setInstanceNetworkType(String instanceNetworkType) {
        this.instanceNetworkType = instanceNetworkType;
    }

    /**
     * 获取实例的访问模式
     *
     * @return connection_mode - 实例的访问模式
     */
    public String getConnectionMode() {
        return connectionMode;
    }

    /**
     * 设置实例的访问模式
     *
     * @param connectionMode 实例的访问模式
     */
    public void setConnectionMode(String connectionMode) {
        this.connectionMode = connectionMode;
    }

    /**
     * 获取实例的网络连接类型
     *
     * @return db_instance_net_type - 实例的网络连接类型
     */
    public String getDbInstanceNetType() {
        return dbInstanceNetType;
    }

    /**
     * 设置实例的网络连接类型
     *
     * @param dbInstanceNetType 实例的网络连接类型
     */
    public void setDbInstanceNetType(String dbInstanceNetType) {
        this.dbInstanceNetType = dbInstanceNetType;
    }

    /**
     * 获取实例储存类型
     *
     * @return db_Instance_storage_type - 实例储存类型
     */
    public String getDbInstanceStorageType() {
        return dbInstanceStorageType;
    }

    /**
     * 设置实例储存类型
     *
     * @param dbInstanceStorageType 实例储存类型
     */
    public void setDbInstanceStorageType(String dbInstanceStorageType) {
        this.dbInstanceStorageType = dbInstanceStorageType;
    }

    /**
     * 获取实例规格
     *
     * @return db_Instance_class - 实例规格
     */
    public String getDbInstanceClass() {
        return dbInstanceClass;
    }

    /**
     * 设置实例规格
     *
     * @param dbInstanceClass 实例规格
     */
    public void setDbInstanceClass(String dbInstanceClass) {
        this.dbInstanceClass = dbInstanceClass;
    }

    /**
     * 获取创建时间
     *
     * @return created_time - 创建时间
     */
    public Date getCreatedTime() {
        return createdTime;
    }

    /**
     * 设置创建时间
     *
     * @param createdTime 创建时间
     */
    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    /**
     * 获取实例系列
     *
     * @return category - 实例系列
     */
    public String getCategory() {
        return category;
    }

    /**
     * 设置实例系列
     *
     * @param category 实例系列
     */
    public void setCategory(String category) {
        this.category = category;
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