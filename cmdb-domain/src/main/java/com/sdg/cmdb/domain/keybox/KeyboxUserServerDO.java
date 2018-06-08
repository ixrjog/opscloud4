package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.todo.TodoKeyboxDetailDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/11/20.
 */
public class KeyboxUserServerDO implements Serializable {
    private static final long serialVersionUID = -3865937011010761144L;

    private long id;

    /*
    用户名
    */
    private String username;

    /*
    服务器组id
     */
    private long serverGroupId;

    private String gmtCreate;

    private String gmtModify;

    public KeyboxUserServerDO() {
    }

    public KeyboxUserServerDO(String username, long serverGroupId) {
        this.username = username;
        this.serverGroupId = serverGroupId;
    }

    public KeyboxUserServerDO(KeyboxUserServerVO userServerVO) {
        this.id = userServerVO.getId();
        this.username = userServerVO.getUsername();
        this.serverGroupId = userServerVO.getServerGroupDO().getId();
        this.gmtCreate = userServerVO.getGmtCreate();
        this.gmtModify = userServerVO.getGmtModify();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
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
        return "KeyboxUserServerDO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", serverGroupId=" + serverGroupId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
