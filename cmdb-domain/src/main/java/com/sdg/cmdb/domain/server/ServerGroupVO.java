package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.config.ConfigPropertyGroupDO;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 2016/10/31.
 */
public class ServerGroupVO implements Serializable {
    private static final long serialVersionUID = -6802315117466446186L;

    private long id;

    private String name;

    private String content;

    private String ipNetwork;

    private int useType;

    private String gmtCreate;

    private String gmtModify;

    private long serverCnt;

    private int keyboxCnt;

    private int userCnt;

    private ServerGroupUseTypeDO serverGroupUseTypeDO;

    private List<String> users;

    private List<ConfigPropertyGroupDO> groupDOList;

    public ServerGroupVO() {
    }

    public ServerGroupVO(ServerGroupDO groupDO, List<ConfigPropertyGroupDO> groupDOList) {
        this.id = groupDO.getId();
        this.name = groupDO.getName();
        this.content = groupDO.getContent();
        this.ipNetwork = groupDO.getIpNetwork();
        this.useType = groupDO.getUseType();
        this.gmtCreate = groupDO.getGmtCreate();
        this.gmtModify = groupDO.getGmtModify();
        this.groupDOList = groupDOList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIpNetwork() {
        return ipNetwork;
    }

    public void setIpNetwork(String ipNetwork) {
        this.ipNetwork = ipNetwork;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
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

    public List<ConfigPropertyGroupDO> getGroupDOList() {
        return groupDOList;
    }

    public void setGroupDOList(List<ConfigPropertyGroupDO> groupDOList) {
        this.groupDOList = groupDOList;
    }

    public long getServerCnt() {
        return serverCnt;
    }

    public void setServerCnt(long serverCnt) {
        this.serverCnt = serverCnt;
    }

    public int getKeyboxCnt() {
        return keyboxCnt;
    }

    public void setKeyboxCnt(int keyboxCnt) {
        this.keyboxCnt = keyboxCnt;
    }

    public int getUserCnt() {
        return userCnt;
    }

    public void setUserCnt(int userCnt) {
        this.userCnt = userCnt;
    }

    public List<String> getUsers() {
        return users;
    }

    public void setUsers(List<String> users) {
        this.users = users;
    }

    public ServerGroupUseTypeDO getServerGroupUseTypeDO() {
        return serverGroupUseTypeDO;
    }

    public void setServerGroupUseTypeDO(ServerGroupUseTypeDO serverGroupUseTypeDO) {
        this.serverGroupUseTypeDO = serverGroupUseTypeDO;
    }

    @Override
    public String toString() {
        return "ServerGroupVO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", content='" + content + '\'' +
                ", ipNetwork='" + ipNetwork + '\'' +
                ", useType=" + useType +
                ", serverCnt=" + serverCnt +
                ", keyboxCnt=" + keyboxCnt +
                ", users='" + users + '\'' +
                ", userCnt=" + userCnt +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", groupDOList=" + groupDOList +
                '}';
    }
}
