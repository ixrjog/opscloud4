package com.sdg.cmdb.domain.keybox;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.domain.todo.TodoDetailDO;
import com.sdg.cmdb.domain.todo.TodoKeyboxDetailDO;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/11/20.
 */
public class KeyboxUserServerVO implements Serializable {
    private static final long serialVersionUID = -8968465839595317281L;

    private long id;

    //前端参数用来确认是否同时添加持续集成组
    private boolean ciChoose;

    /*
    用户名
    */
    private String username;

    /*
    服务器组id
     */
    private long serverGroupId;

    private String displayName;

    private String mail;
    /*
    服务器组
     */
    private ServerGroupDO serverGroupDO;

    private String gmtCreate;

    private String gmtModify;

    /**
     * 1 在zabbix用户组中
     * 0 不再zabbix用户组中
     */
    private int zabbixUsergroup = 0;

    public KeyboxUserServerVO() {
    }


    public KeyboxUserServerVO(String username, TodoKeyboxDetailDO todoKeyboxDetailDO) {
        this.serverGroupId = todoKeyboxDetailDO.getServerGroupId();
        this.ciChoose = todoKeyboxDetailDO.isCiAuth();
        this.username = username;
    }

    public KeyboxUserServerVO(KeyboxUserServerDO userServerDO, ServerGroupDO serverGroupDO, UserDO userDO) {
        this.id = userServerDO.getId();
        this.username = userServerDO.getUsername();

        this.serverGroupDO = serverGroupDO;
        this.gmtCreate = userServerDO.getGmtCreate();
        this.gmtModify = userServerDO.getGmtModify();
        this.displayName = userDO.getDisplayName();
        this.mail = userDO.getMail();
    }

    public KeyboxUserServerVO(KeyboxUserServerDO userServerDO, ServerGroupDO serverGroupDO) {
        this.id = userServerDO.getId();
        this.username = userServerDO.getUsername();

        this.serverGroupDO = serverGroupDO;
        this.gmtCreate = userServerDO.getGmtCreate();
        this.gmtModify = userServerDO.getGmtModify();
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

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
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

    public int getZabbixUsergroup() {
        return zabbixUsergroup;
    }

    public void setZabbixUsergroup(int zabbixUsergroup) {
        this.zabbixUsergroup = zabbixUsergroup;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public boolean isCiChoose() {
        return ciChoose;
    }

    public void setCiChoose(boolean ciChoose) {
        this.ciChoose = ciChoose;
    }

    @Override
    public String toString() {
        return "KeyboxUserServerVO{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", displayName='" + displayName + '\'' +
                ", mail='" + mail + '\'' +
                ", serverGroupDO=" + serverGroupDO +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

}
