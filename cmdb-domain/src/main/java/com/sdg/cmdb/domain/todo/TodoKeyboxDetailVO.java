package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.springframework.util.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/7.
 */
public class TodoKeyboxDetailVO implements Serializable {
    private static final long serialVersionUID = 1722701837373377789L;

    private long id;

    private long todoDetailId;

    private long serverGroupId;

    private String serverGroupName;

    private String serverGroupContent;

    private boolean ciAuth;

    private long ciUserGroupId;

    private String ciUserGroupName;

    // 权限详情
    private String content;

    /**
     * 处理状态
     */
    private int processStatus;

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoKeyboxDetailVO{" +
                "id=" + id +
                ", todoDetailId=" + todoDetailId +
                ", serverGroupId=" + serverGroupId +
                ", serverGroupName='" + serverGroupName + '\'' +
                ", serverGroupContent='" + serverGroupContent + '\'' +
                ", ciAuth=" + ciAuth +
                ", ciUserGroupId=" + ciUserGroupId +
                ", ciUserGroupName='" + ciUserGroupName + '\'' +
                ", processStatus=" + processStatus +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }

    public TodoKeyboxDetailVO(TodoKeyboxDetailDO todoKeyboxDetailDO, ServerGroupDO serverGroupDO) {
        this.id = todoKeyboxDetailDO.getId();
        this.todoDetailId = todoKeyboxDetailDO.getTodoDetailId();
        this.serverGroupId = todoKeyboxDetailDO.getServerGroupId();
        if(serverGroupDO != null){
            this.serverGroupName = serverGroupDO.getName();
            this.serverGroupContent = serverGroupDO.getContent();
        }else{
            this.serverGroupName = "服务器组已下线";
        }
        this.ciAuth = todoKeyboxDetailDO.isCiAuth();
        this.ciUserGroupId = todoKeyboxDetailDO.getCiUserGroupId();
        this.ciUserGroupName = todoKeyboxDetailDO.getCiUserGroupName();
        this.processStatus = todoKeyboxDetailDO.getProcessStatus();
        this.gmtCreate = todoKeyboxDetailDO.getGmtCreate();
        this.gmtModify = todoKeyboxDetailDO.getGmtModify();
    }

    public TodoKeyboxDetailVO() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getTodoDetailId() {
        return todoDetailId;
    }

    public void setTodoDetailId(long todoDetailId) {
        this.todoDetailId = todoDetailId;
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

    public String getServerGroupContent() {
        return serverGroupContent;
    }

    public void setServerGroupContent(String serverGroupContent) {
        this.serverGroupContent = serverGroupContent;
    }

    public boolean isCiAuth() {
        return ciAuth;
    }

    public void setCiAuth(boolean ciAuth) {
        this.ciAuth = ciAuth;
    }

    public long getCiUserGroupId() {
        return ciUserGroupId;
    }

    public void setCiUserGroupId(long ciUserGroupId) {
        this.ciUserGroupId = ciUserGroupId;
    }

    public String getCiUserGroupName() {
        return ciUserGroupName;
    }

    public void setCiUserGroupName(String ciUserGroupName) {
        this.ciUserGroupName = ciUserGroupName;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getContent() {
        if (ciAuth && ciUserGroupId != 0) {
            return "服务器组:" + this.serverGroupName + "和持续集成组:" + ciUserGroupName + ";";
        } else {
            return "服务器组:" + this.serverGroupName + ";";
        }
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
