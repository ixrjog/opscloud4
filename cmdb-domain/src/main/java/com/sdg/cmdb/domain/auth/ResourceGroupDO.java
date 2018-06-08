package com.sdg.cmdb.domain.auth;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/19.
 */
public class ResourceGroupDO implements Serializable {
    private static final long serialVersionUID = -3622624141413375269L;

    public static final String menu = "menu";

    private long id;

    private String groupCode;

    private String groupDesc;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getGroupDesc() {
        return groupDesc;
    }

    public void setGroupDesc(String groupDesc) {
        this.groupDesc = groupDesc;
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
        return "ResourceGroupDO{" +
                "id=" + id +
                ", groupCode='" + groupCode + '\'' +
                ", groupDesc='" + groupDesc + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
