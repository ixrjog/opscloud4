package com.baiyi.opscloud.domain.generator.opscloud;

import javax.persistence.*;
import java.util.Date;

@Table(name = "oc_setting")
public class OcSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    /**
     * 设置组
     */
    @Column(name = "setting_group")
    private String settingGroup;

    /**
     * 设置名称
     */
    private String name;

    /**
     * 设置值
     */
    @Column(name = "setting_value")
    private String settingValue;

    /**
     * 是否加密
     */
    private Boolean encrypted;

    private String comment;

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
     * 获取设置组
     *
     * @return setting_group - 设置组
     */
    public String getSettingGroup() {
        return settingGroup;
    }

    /**
     * 设置设置组
     *
     * @param settingGroup 设置组
     */
    public void setSettingGroup(String settingGroup) {
        this.settingGroup = settingGroup;
    }

    /**
     * 获取设置名称
     *
     * @return name - 设置名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置设置名称
     *
     * @param name 设置名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取设置值
     *
     * @return setting_value - 设置值
     */
    public String getSettingValue() {
        return settingValue;
    }

    /**
     * 设置设置值
     *
     * @param settingValue 设置值
     */
    public void setSettingValue(String settingValue) {
        this.settingValue = settingValue;
    }

    /**
     * 获取是否加密
     *
     * @return encrypted - 是否加密
     */
    public Boolean getEncrypted() {
        return encrypted;
    }

    /**
     * 设置是否加密
     *
     * @param encrypted 是否加密
     */
    public void setEncrypted(Boolean encrypted) {
        this.encrypted = encrypted;
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