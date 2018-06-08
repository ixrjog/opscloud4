package com.sdg.cmdb.domain.config;


import java.io.Serializable;

/**
 * 配置同步到服务器配置
 */
public class ConfigFileCopyDO implements Serializable {


    public ConfigFileCopyDO(){

    }

    public ConfigFileCopyDO( ConfigFileCopyVO configFileCopyVO){
        this.id = configFileCopyVO.getId();
        this.groupId = configFileCopyVO.getGroupId();
        this.groupName = configFileCopyVO.getGroupName();
        this.serverId = configFileCopyVO.getServerDO().getId();
        this.src = configFileCopyVO.getSrc();
        this.dest = configFileCopyVO.getDest();
        this.username = configFileCopyVO.getUsername();
        this.usergroup = configFileCopyVO.getUsergroup();
    }

    private long id;

    // 文件组
    private long groupId;

    // 文件组
    private String groupName;

    private long serverId;

    // 源路径
    private String src;
    // 目标路径
    private String dest;

    // 用户 owner
    private String username = "root";
    // 用户组 group
    private String usergroup = "root";

    private String gmtCreate;

    private String gmtModify;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
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
