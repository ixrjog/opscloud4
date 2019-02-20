package com.sdg.cmdb.domain.ci;

import java.io.Serializable;

public class CiTemplateDO implements Serializable {
    private static final long serialVersionUID = 7043852684095702649L;

    private long id;

    private String name;

    private int version;

    private int appType;

    private int ciType;

    private int rollbackType;

    private int envType;

    private String md5;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public CiTemplateDO(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getAppType() {
        return appType;
    }

    public void setAppType(int appType) {
        this.appType = appType;
    }

    public int getCiType() {
        return ciType;
    }

    public void setCiType(int ciType) {
        this.ciType = ciType;
    }

    public int getRollbackType() {
        return rollbackType;
    }

    public void setRollbackType(int rollbackType) {
        this.rollbackType = rollbackType;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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
}
