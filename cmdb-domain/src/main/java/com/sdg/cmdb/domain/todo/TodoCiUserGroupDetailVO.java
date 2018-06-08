package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.apache.commons.lang.StringUtils;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/11.
 */
public class TodoCiUserGroupDetailVO implements Serializable {
    private static final long serialVersionUID = 6981746198009874195L;

    private long id;

    private long todoDetailId;


    private long ciUserGroupId;

    private String ciUserGroupName;

    private long serverGroupId;

    private String serverGroupName;

    private String serverGroupContent;

    private int envType;

    // 权限详情
    private String content;
    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int processStatus;

    private String gmtCreate;

    private String gmtModify;

    public TodoCiUserGroupDetailVO(TodoCiUserGroupDetailDO todoCiUserGroupDetailDO, ServerGroupDO serverGroupDO) {
        this.id = todoCiUserGroupDetailDO.getId();
        this.todoDetailId = todoCiUserGroupDetailDO.getTodoDetailId();
        this.ciUserGroupId = todoCiUserGroupDetailDO.getCiUserGroupId();
        this.ciUserGroupName = todoCiUserGroupDetailDO.getCiUserGroupName();
        this.serverGroupId = todoCiUserGroupDetailDO.getServerGroupId();
        this.serverGroupName = serverGroupDO.getName();
        this.serverGroupContent = serverGroupDO.getContent();
        this.envType = todoCiUserGroupDetailDO.getEnvType();
        this.processStatus = todoCiUserGroupDetailDO.getProcessStatus();
        this.gmtCreate = todoCiUserGroupDetailDO.getGmtCreate();
        this.gmtModify = todoCiUserGroupDetailDO.getGmtModify();
    }


    public TodoCiUserGroupDetailVO(TodoCiUserGroupDetailDO todoCiUserGroupDetailDO) {
        this.id = todoCiUserGroupDetailDO.getId();
        this.todoDetailId = todoCiUserGroupDetailDO.getTodoDetailId();
        this.ciUserGroupId = todoCiUserGroupDetailDO.getCiUserGroupId();
        this.ciUserGroupName = todoCiUserGroupDetailDO.getCiUserGroupName();

        this.envType = todoCiUserGroupDetailDO.getEnvType();
        this.processStatus = todoCiUserGroupDetailDO.getProcessStatus();
        this.gmtCreate = todoCiUserGroupDetailDO.getGmtCreate();
        this.gmtModify = todoCiUserGroupDetailDO.getGmtModify();
    }

    public TodoCiUserGroupDetailVO() {

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

    public int getEnvType() {
        return envType;
    }

    public void setEnvType(int envType) {
        this.envType = envType;
    }

    public int getProcessStatus() {
        return processStatus;
    }

    public void setProcessStatus(int processStatus) {
        this.processStatus = processStatus;
    }

    public String getContent() {
        return "持续集成组:" + ciUserGroupName + ";";
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
