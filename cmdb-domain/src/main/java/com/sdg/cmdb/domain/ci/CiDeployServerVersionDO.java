package com.sdg.cmdb.domain.ci;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/4/13.
 */
public class CiDeployServerVersionDO implements Serializable {

    private static final long serialVersionUID = -3517029840767339893L;

    private long id;

    private long serverId;

    private long ciDeployStatisticsId;

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }


    public long getCiDeployStatisticsId() {
        return ciDeployStatisticsId;
    }

    public void setCiDeployStatisticsId(long ciDeployStatisticsId) {
        this.ciDeployStatisticsId = ciDeployStatisticsId;
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

    public CiDeployServerVersionDO(){

    }

    public CiDeployServerVersionDO(long serverId, long ciDeployStatisticsId){
        this.serverId = serverId;
        this.ciDeployStatisticsId = ciDeployStatisticsId;

    }

    @Override
    public String toString() {
        return "CiDeployServerVersionDO{" +
                ", id=" + id +
                ", serverId=" + serverId +
                ", ciDeployStatisticsId=" + ciDeployStatisticsId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }




}
