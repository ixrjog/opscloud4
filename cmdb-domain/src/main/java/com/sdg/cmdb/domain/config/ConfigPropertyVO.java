package com.sdg.cmdb.domain.config;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/20.
 */
public class ConfigPropertyVO implements Serializable {
    private static final long serialVersionUID = -6198021597822505240L;

    private long id;

    /*
    属性名称
     */
    private String proName;

    /*
    属性默认值
     */
    private String proValue;

    /*
    属性描述
     */
    private String proDesc;

    /*
    组
     */
    private ConfigPropertyGroupDO groupDO;

    private String gmtCreate;

    private String gmtModify;

    public ConfigPropertyVO(ConfigPropertyDO propertyDO, ConfigPropertyGroupDO groupDO) {
        this.id = propertyDO.getId();
        this.proName = propertyDO.getProName();
        this.proValue = propertyDO.getProValue();
        this.proDesc = propertyDO.getProDesc();
        this.groupDO = groupDO;
        this.gmtCreate = propertyDO.getGmtCreate();
        this.gmtModify = propertyDO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProName() {
        return proName;
    }

    public void setProName(String proName) {
        this.proName = proName;
    }

    public String getProValue() {
        return proValue;
    }

    public void setProValue(String proValue) {
        this.proValue = proValue;
    }

    public String getProDesc() {
        return proDesc;
    }

    public void setProDesc(String proDesc) {
        this.proDesc = proDesc;
    }

    public ConfigPropertyGroupDO getGroupDO() {
        return groupDO;
    }

    public void setGroupDO(ConfigPropertyGroupDO groupDO) {
        this.groupDO = groupDO;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    @Override
    public String toString() {
        return "ConfigPropertyVO{" +
                "id=" + id +
                ", proName='" + proName + '\'' +
                ", proValue='" + proValue + '\'' +
                ", proDesc='" + proDesc + '\'' +
                ", groupDO=" + groupDO +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
