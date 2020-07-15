package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_server_group_property")
public class OcServerGroupProperty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 服务器组id
     */
    @Column(name = "server_group_id")
    private Integer serverGroupId;

    /**
     * 环境type
     */
    @Column(name = "env_type")
    private Integer envType;

    /**
     * 属性名称
     */
    @Column(name = "property_name")
    private String propertyName;

    /**
     * 属性值
     */
    @Column(name = "property_value")
    private String propertyValue;

    @Column(name = "create_time", insertable = false, updatable = false)
    private Date createTime;

    @Column(name = "update_time", insertable = false, updatable = false)
    private Date updateTime;

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
     * 获取服务器组id
     *
     * @return server_group_id - 服务器组id
     */
    public Integer getServerGroupId() {
        return serverGroupId;
    }

    /**
     * 设置服务器组id
     *
     * @param serverGroupId 服务器组id
     */
    public void setServerGroupId(Integer serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    /**
     * 获取环境type
     *
     * @return env_type - 环境type
     */
    public Integer getEnvType() {
        return envType;
    }

    /**
     * 设置环境type
     *
     * @param envType 环境type
     */
    public void setEnvType(Integer envType) {
        this.envType = envType;
    }

    /**
     * 获取属性名称
     *
     * @return property_name - 属性名称
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 设置属性名称
     *
     * @param propertyName 属性名称
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 获取属性值
     *
     * @return property_value - 属性值
     */
    public String getPropertyValue() {
        return propertyValue;
    }

    /**
     * 设置属性值
     *
     * @param propertyValue 属性值
     */
    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
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
}