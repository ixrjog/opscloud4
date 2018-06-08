package com.sdg.cmdb.domain.explain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 2017/3/22.
 */
public class ExplainDTO implements Serializable {
    private static final long serialVersionUID = -7159199317295739466L;

    private long id;

    private String repo;

    private List<String> scanPathList;

    private List<String> notifyEmailList;

    /**
     * 关联数据库实例 cdl appid
     */
    private String cdlAppId;

    /**
     * 关联数据库实例 cdl group
     */
    private String cdlGroup;

    private String gmtCreate;

    private String gmtModify;

    public ExplainDTO() {
    }

    public ExplainDTO(ExplainInfo explainInfo) {
        this.id = explainInfo.getId();
        this.repo = explainInfo.getRepo();
        this.scanPathList = JSON.parseArray(explainInfo.getScanPath(), String.class);
        this.notifyEmailList = JSON.parseArray(explainInfo.getNotifyEmails(), String.class);
        this.cdlAppId = explainInfo.getCdlAppId();
        this.cdlGroup = explainInfo.getCdlGroup();
        this.gmtCreate = explainInfo.getGmtCreate();
        this.gmtModify = explainInfo.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRepo() {
        return repo;
    }

    public void setRepo(String repo) {
        this.repo = repo;
    }

    public List<String> getScanPathList() {
        return scanPathList;
    }

    public void setScanPathList(List<String> scanPathList) {
        this.scanPathList = scanPathList;
    }

    public List<String> getNotifyEmailList() {
        return notifyEmailList;
    }

    public void setNotifyEmailList(List<String> notifyEmailList) {
        this.notifyEmailList = notifyEmailList;
    }

    public String getCdlAppId() {
        return cdlAppId;
    }

    public void setCdlAppId(String cdlAppId) {
        this.cdlAppId = cdlAppId;
    }

    public String getCdlGroup() {
        return cdlGroup;
    }

    public void setCdlGroup(String cdlGroup) {
        this.cdlGroup = cdlGroup;
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
        return "ExplainDTO{" +
                "id=" + id +
                ", repo='" + repo + '\'' +
                ", scanPathList=" + scanPathList +
                ", notifyEmailList=" + notifyEmailList +
                ", cdlAppId='" + cdlAppId + '\'' +
                ", cdlGroup='" + cdlGroup + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
