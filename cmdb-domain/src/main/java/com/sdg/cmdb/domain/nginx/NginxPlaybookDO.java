package com.sdg.cmdb.domain.nginx;

import java.io.Serializable;

public class NginxPlaybookDO implements Serializable {
    private static final long serialVersionUID = -6501178287841134420L;

    private long id;

    private long vhostId;

    private String serverKey;

    private long serverGroupId;
    // 仅查看
    private String serverGroupName;

    /**
     * ansible host
     */
    private String hostPattern;

    // 源路径
    private String src;
    // 目标路径
    private String dest;

    // 用户 owner
    private String username = "root";
    // 用户组 group
    private String usergroup = "root";

    private long playbookId;

    private String gmtCreate;

    private String gmtModify;

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

    public String getServerKey() {
        return serverKey;
    }

    public void setServerKey(String serverKey) {
        this.serverKey = serverKey;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public String getServerGroupName() {
        return serverGroupName;
    }

    public void setServerGroupName(String serverGroupName) {
        this.serverGroupName = serverGroupName;
    }

    public String getHostPattern() {
        return hostPattern;
    }

    public void setHostPattern(String hostPattern) {
        this.hostPattern = hostPattern;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsergroup() {
        return usergroup;
    }

    public void setUsergroup(String usergroup) {
        this.usergroup = usergroup;
    }

    public long getPlaybookId() {
        return playbookId;
    }

    public void setPlaybookId(long playbookId) {
        this.playbookId = playbookId;
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
