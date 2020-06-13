package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_db_database")
public class OcCloudDbDatabase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 云数据库id
     */
    @Column(name = "cloud_db_id")
    private Integer cloudDbId;

    /**
     * 云数据库实例id
     */
    @Column(name = "db_instance_id")
    private String dbInstanceId;

    /**
     * 数据库名称
     */
    @Column(name = "db_name")
    private String dbName;

    /**
     * 数据库实例类型
     */
    private String engine;

    /**
     * 描述
     */
    @Column(name = "db_description")
    private String dbDescription;

    /**
     * 数据库状态，取值：

Creating：创建中
Running：使用中
Deleting：删除中
     */
    @Column(name = "db_status")
    private String dbStatus;

    /**
     * 字符集
     */
    @Column(name = "character_set_name")
    private String characterSetName;

    /**
     * 环境类型
     */
    @Column(name = "env_type")
    private Integer envType;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 备注
     */
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
     * 获取云数据库id
     *
     * @return cloud_db_id - 云数据库id
     */
    public Integer getCloudDbId() {
        return cloudDbId;
    }

    /**
     * 设置云数据库id
     *
     * @param cloudDbId 云数据库id
     */
    public void setCloudDbId(Integer cloudDbId) {
        this.cloudDbId = cloudDbId;
    }

    /**
     * 获取云数据库实例id
     *
     * @return db_instance_id - 云数据库实例id
     */
    public String getDbInstanceId() {
        return dbInstanceId;
    }

    /**
     * 设置云数据库实例id
     *
     * @param dbInstanceId 云数据库实例id
     */
    public void setDbInstanceId(String dbInstanceId) {
        this.dbInstanceId = dbInstanceId;
    }

    /**
     * 获取数据库名称
     *
     * @return db_name - 数据库名称
     */
    public String getDbName() {
        return dbName;
    }

    /**
     * 设置数据库名称
     *
     * @param dbName 数据库名称
     */
    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * 获取数据库实例类型
     *
     * @return engine - 数据库实例类型
     */
    public String getEngine() {
        return engine;
    }

    /**
     * 设置数据库实例类型
     *
     * @param engine 数据库实例类型
     */
    public void setEngine(String engine) {
        this.engine = engine;
    }

    /**
     * 获取描述
     *
     * @return db_description - 描述
     */
    public String getDbDescription() {
        return dbDescription;
    }

    /**
     * 设置描述
     *
     * @param dbDescription 描述
     */
    public void setDbDescription(String dbDescription) {
        this.dbDescription = dbDescription;
    }

    /**
     * 获取数据库状态，取值：

Creating：创建中
Running：使用中
Deleting：删除中
     *
     * @return db_status - 数据库状态，取值：

Creating：创建中
Running：使用中
Deleting：删除中
     */
    public String getDbStatus() {
        return dbStatus;
    }

    /**
     * 设置数据库状态，取值：

Creating：创建中
Running：使用中
Deleting：删除中
     *
     * @param dbStatus 数据库状态，取值：

Creating：创建中
Running：使用中
Deleting：删除中
     */
    public void setDbStatus(String dbStatus) {
        this.dbStatus = dbStatus;
    }

    /**
     * 获取字符集
     *
     * @return character_set_name - 字符集
     */
    public String getCharacterSetName() {
        return characterSetName;
    }

    /**
     * 设置字符集
     *
     * @param characterSetName 字符集
     */
    public void setCharacterSetName(String characterSetName) {
        this.characterSetName = characterSetName;
    }

    /**
     * 获取环境类型
     *
     * @return env_type - 环境类型
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境类型
     *
     * @param envType 环境类型
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
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
     * 获取备注
     *
     * @return comment - 备注
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置备注
     *
     * @param comment 备注
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}