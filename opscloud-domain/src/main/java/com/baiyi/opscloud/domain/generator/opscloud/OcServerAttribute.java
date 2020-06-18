package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_attribute")
public class OcServerAttribute {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 业务id
     */
    @Column(name = "business_id")
    private Integer businessId;

    /**
     * 业务类型
     */
    @Column(name = "business_type")
    private Integer businessType;

    /**
     * 属性类型
     */
    @Column(name = "attribute_type")
    private String attributeType;

    /**
     * 属性组名
     */
    @Column(name = "group_name")
    private String groupName;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

    /**
     * 属性配置项
     */
    private String attributes;

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
     * 获取业务id
     *
     * @return business_id - 业务id
     */
    public Integer getBusinessId() {
        return businessId;
    }

    /**
     * 设置业务id
     *
     * @param businessId 业务id
     */
    public void setBusinessId(Integer businessId) {
        this.businessId = businessId;
    }

    /**
     * 获取业务类型
     *
     * @return business_type - 业务类型
     */
    public Integer getBusinessType() {
        return businessType;
    }

    /**
     * 设置业务类型
     *
     * @param businessType 业务类型
     */
    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    /**
     * 获取属性类型
     *
     * @return attribute_type - 属性类型
     */
    public String getAttributeType() {
        return attributeType;
    }

    /**
     * 设置属性类型
     *
     * @param attributeType 属性类型
     */
    public void setAttributeType(String attributeType) {
        this.attributeType = attributeType;
    }

    /**
     * 获取属性组名
     *
     * @return group_name - 属性组名
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * 设置属性组名
     *
     * @param groupName 属性组名
     */
    public void setGroupName(String groupName) {
        this.groupName = groupName;
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
     * 获取属性配置项
     *
     * @return attributes - 属性配置项
     */
    public String getAttributes() {
        return attributes;
    }

    /**
     * 设置属性配置项
     *
     * @param attributes 属性配置项
     */
    public void setAttributes(String attributes) {
        this.attributes = attributes;
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