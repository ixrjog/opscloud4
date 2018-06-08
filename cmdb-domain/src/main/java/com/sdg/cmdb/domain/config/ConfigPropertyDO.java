package com.sdg.cmdb.domain.config;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/10/20.
 */
public class ConfigPropertyDO implements Serializable {
    private static final long serialVersionUID = 2140523078156398557L;

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
    组id
     */
    private long groupId;

    private String gmtCreate;

    private String gmtModify;

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

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
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
        return "ConfigPropertyDO{" +
                "id=" + id +
                ", proName='" + proName + '\'' +
                ", proValue='" + proValue + '\'' +
                ", proDesc='" + proDesc + '\'' +
                ", groupId=" + groupId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
