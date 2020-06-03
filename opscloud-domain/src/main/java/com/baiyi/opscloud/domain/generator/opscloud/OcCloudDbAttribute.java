package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_cloud_db_attribute")
public class OcCloudDbAttribute {
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
     * 属性名称
     */
    @Column(name = "attribute_name")
    private String attributeName;

    /**
     * 属性值
     */
    @Column(name = "attribute_value")
    private String attributeValue;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 描述
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
     * 获取属性名称
     *
     * @return attribute_name - 属性名称
     */
    public String getAttributeName() {
        return attributeName;
    }

    /**
     * 设置属性名称
     *
     * @param attributeName 属性名称
     */
    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    /**
     * 获取属性值
     *
     * @return attribute_value - 属性值
     */
    public String getAttributeValue() {
        return attributeValue;
    }

    /**
     * 设置属性值
     *
     * @param attributeValue 属性值
     */
    public void setAttributeValue(String attributeValue) {
        this.attributeValue = attributeValue;
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
     * 获取描述
     *
     * @return comment - 描述
     */
    public String getComment() {
        return comment;
    }

    /**
     * 设置描述
     *
     * @param comment 描述
     */
    public void setComment(String comment) {
        this.comment = comment;
    }
}