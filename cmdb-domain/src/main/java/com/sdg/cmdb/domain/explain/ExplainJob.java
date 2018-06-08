package com.sdg.cmdb.domain.explain;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/4/11.
 */
public class ExplainJob implements Serializable {
    private static final long serialVersionUID = 2631273712950617801L;

    private long id;

    /**
     * 源计划Id
     */
    private long metaId;

    /**
     * 任务分支
     */
    private String jobBranch;

    /**
     * 任务权重 默认设置为1
     */
    private int jobWeight = 1;

    /**
     * 任务版本
     */
    private int jobVersion;

    /**
     * 任务状态.-1：无效；0：待开始；1：执行中；2：执行异常；3：执行完成
     */
    private Integer jobStatus;

    /**
     * 唯一键
     */
    private long uniqueField;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getMetaId() {
        return metaId;
    }

    public void setMetaId(long metaId) {
        this.metaId = metaId;
    }

    public String getJobBranch() {
        return jobBranch;
    }

    public void setJobBranch(String jobBranch) {
        this.jobBranch = jobBranch;
    }

    public int getJobWeight() {
        return jobWeight;
    }

    public void setJobWeight(int jobWeight) {
        this.jobWeight = jobWeight;
    }

    public int getJobVersion() {
        return jobVersion;
    }

    public void setJobVersion(int jobVersion) {
        this.jobVersion = jobVersion;
    }

    public Integer getJobStatus() {
        return jobStatus;
    }

    public void setJobStatus(Integer jobStatus) {
        this.jobStatus = jobStatus;
    }

    public long getUniqueField() {
        return uniqueField;
    }

    public void setUniqueField(long uniqueField) {
        this.uniqueField = uniqueField;
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
        return "ExplainJob{" +
                "id=" + id +
                ", metaId=" + metaId +
                ", jobBranch='" + jobBranch + '\'' +
                ", jobWeight=" + jobWeight +
                ", jobVersion=" + jobVersion +
                ", jobStatus=" + jobStatus +
                ", uniqueField=" + uniqueField +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
