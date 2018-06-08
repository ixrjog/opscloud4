package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

public class BuildArtifactDO implements Serializable {

    private static final long serialVersionUID = -5817129277776433685L;

    private long id;

    private long jobBuildsId;

    private String artifactsName;

    private String archiveUrl;

    private String gmtCreate;

    private String gmtModify;

    public BuildArtifactDO() {

    }

    public BuildArtifactDO(long jobBuildsId, String artifactsName, String archiveUrl) {
        this.jobBuildsId = jobBuildsId;
        this.artifactsName = artifactsName;
        this.archiveUrl = archiveUrl;
    }


    @Override
    public String toString() {
        return "BuildArtifactDO{" +
                ", id=" + id +
                ", jobBuildsId=" + jobBuildsId +
                ", artifactsName='" + artifactsName + '\'' +
                ", archiveUrl='" + archiveUrl + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getJobBuildsId() {
        return jobBuildsId;
    }

    public void setJobBuildsId(long jobBuildsId) {
        this.jobBuildsId = jobBuildsId;
    }

    public String getArtifactsName() {
        return artifactsName;
    }

    public void setArtifactsName(String artifactsName) {
        this.artifactsName = artifactsName;
    }

    public String getArchiveUrl() {
        return archiveUrl;
    }

    public void setArchiveUrl(String archiveUrl) {
        this.archiveUrl = archiveUrl;
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
