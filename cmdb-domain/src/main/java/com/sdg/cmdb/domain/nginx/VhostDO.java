package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;

public class VhostDO implements Serializable {
    private static final long serialVersionUID = -217764746326894611L;

    private long id;

    private String serverName;

    private String serverKey;

    private String content;

    private String gmtCreate;

    private String gmtModify;


    public VhostDO(){

    }

    public VhostDO(VhostVO vhostVO){
        this.id = vhostVO.getId();
        this.serverName = vhostVO.getServerName();
        this.serverKey = vhostVO.getServerKey();
        this.content = vhostVO.getContent();
    }

    @Override
    public String toString() {
        return "VhostDO{" +
                "id=" + id +
                ", serverName='" + serverName + '\'' +
                ", serverKey='" + serverKey + '\'' +
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

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
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
