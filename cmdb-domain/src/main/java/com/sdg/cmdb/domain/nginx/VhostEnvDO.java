package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;

public class VhostEnvDO implements Serializable {
    private static final long serialVersionUID = 2500803503147477395L;

    private long id;

    private long vhostId;

    private int envType;

    /**
     * 相对路径
     * confPath = /data/www/conf/web-server/www.example.com/www/vhost
     */
    private String confPath;

    /**
     * 是否自动生成upstream配置
     */
    private boolean autoBuild = false;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "VhostDO{" +
                "id=" + id +
                ", vhostId=" + vhostId +
                ", envType=" + envType +
                ", confPath='" + confPath + '\'' +
                ", autoBuild='" + autoBuild + '\'' +
                ", content='" + content + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVhostId() {
        return vhostId;
    }

    public void setVhostId(long vhostId) {
        this.vhostId = vhostId;
    }

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }

    public boolean isAutoBuild() {
        return autoBuild;
    }

    public void setAutoBuild(boolean autoBuild) {
        this.autoBuild = autoBuild;
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
