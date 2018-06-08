package com.sdg.cmdb.domain.systems;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/10/8.
 */
public class SystemDO implements Serializable {
    private static final long serialVersionUID = 7300783386547958049L;

    private long id;

    /*
    系统名称
     */
    private String systemName;

    /*
    系统url
     */
    private String systemUrl;

    /*
    图片url
     */
    private String imgUrl;

    /*
    系统介绍
     */
    private String systemDesc;

    /*
    系统负责人
     */
    private String owner;

    /*
    创建时间
     */
    private String gmtCreate;

    /*
    更新时间
     */
    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSystemName() {
        return systemName;
    }

    public void setSystemName(String systemName) {
        this.systemName = systemName;
    }

    public String getSystemUrl() {
        return systemUrl;
    }

    public void setSystemUrl(String systemUrl) {
        this.systemUrl = systemUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getSystemDesc() {
        return systemDesc;
    }

    public void setSystemDesc(String systemDesc) {
        this.systemDesc = systemDesc;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
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
        return "SystemDO{" +
                "id=" + id +
                ", systemName='" + systemName + '\'' +
                ", systemUrl='" + systemUrl + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", systemDesc='" + systemDesc + '\'' +
                ", owner='" + owner + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
