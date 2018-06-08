package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

public class JobBuildParamDO implements Serializable {
    private static final long serialVersionUID = -8638003930122670467L;

    private long id;

    private long buildsId;

    private String paramName;

    private String paramValue;


    private String gmtCreate;

    private String gmtModify;

    public JobBuildParamDO() {

    }

    public JobBuildParamDO(long buildsId, String paramName, String paramValue) {
        this.buildsId = buildsId;
        this.paramName = paramName;
        this.paramValue = paramValue;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBuildsId() {
        return buildsId;
    }

    public void setBuildsId(long buildsId) {
        this.buildsId = buildsId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
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
