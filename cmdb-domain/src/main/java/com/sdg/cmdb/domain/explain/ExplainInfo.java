package com.sdg.cmdb.domain.explain;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/3/21.
 */
public class ExplainInfo implements Serializable {
    private static final long serialVersionUID = -3910881525747056368L;

    private long id;

    /**
     * 仓库
     */
    private String repo;

    /**
     * 扫描路径
     */
    private String scanPath;

    /**
     * 通知集合
     */
    private String notifyEmails;

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

    public ExplainInfo() {
    }

    public ExplainInfo(ExplainDTO explainDTO) {
        this.id = explainDTO.getId();
        this.repo = explainDTO.getRepo();
        this.scanPath = JSON.toJSONString(explainDTO.getScanPathList());
        this.notifyEmails = JSON.toJSONString(explainDTO.getNotifyEmailList());
        this.cdlAppId = explainDTO.getCdlAppId();
        this.cdlGroup = explainDTO.getCdlGroup();
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

    public String getScanPath() {
        return scanPath;
    }

    public void setScanPath(String scanPath) {
        this.scanPath = scanPath;
    }

    public String getNotifyEmails() {
        return notifyEmails;
    }

    public void setNotifyEmails(String notifyEmails) {
        this.notifyEmails = notifyEmails;
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
        return "ExplainInfo{" +
                "id=" + id +
                ", repo='" + repo + '\'' +
                ", scanPath='" + scanPath + '\'' +
                ", notifyEmails='" + notifyEmails + '\'' +
                ", cdlAppId='" + cdlAppId + '\'' +
                ", cdlGroup='" + cdlGroup + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
