package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.auth.UserDO;
import com.sdg.cmdb.domain.auth.UserVO;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/9/6.
 */
public class TodoDetailVO implements Serializable {
    private static final long serialVersionUID = 1635901221304978345L;

    public TodoDetailVO() {

    }

    public TodoDetailVO(TodoDetailDO todoDetailDO) {
        this.id = todoDetailDO.getId();
        this.initiatorUserId = todoDetailDO.getInitiatorUserId();
        this.initiatorUsername = todoDetailDO.getInitiatorUsername();
        this.todoId = todoDetailDO.getTodoId();
        this.assigneeUserId = todoDetailDO.getAssigneeUserId();
        this.assigneeUsername = todoDetailDO.getAssigneeUsername();
        this.approvalUserId = todoDetailDO.getApprovalUserId();
        this.approvalUsername = todoDetailDO.getApprovalUsername();
        this.todoStatus = todoDetailDO.getTodoStatus();
        this.gmtCreate = todoDetailDO.getGmtCreate();
        this.gmtModify = todoDetailDO.getGmtModify();
    }

    public TodoDetailVO(TodoDetailDO todoDetailDO, List<TodoKeyboxDetailVO> todoKeyboxDetailList) {
        //this(todoDetailDO);
        this.id = todoDetailDO.getId();
        this.initiatorUserId = todoDetailDO.getInitiatorUserId();
        this.initiatorUsername = todoDetailDO.getInitiatorUsername();
        this.todoId = todoDetailDO.getTodoId();
        this.assigneeUserId = todoDetailDO.getAssigneeUserId();
        this.assigneeUsername = todoDetailDO.getAssigneeUsername();
        this.approvalUserId = todoDetailDO.getApprovalUserId();
        this.approvalUsername = todoDetailDO.getApprovalUsername();
        this.todoStatus = todoDetailDO.getTodoStatus();
        this.gmtCreate = todoDetailDO.getGmtCreate();
        this.gmtModify = todoDetailDO.getGmtModify();
        this.todoKeyboxDetailList = todoKeyboxDetailList;
    }

    private List<UserDO> assigneeUsers;

    //负责人详情
    private UserDO assigneeUserDO;

    private List<TodoKeyboxDetailVO> todoKeyboxDetailList;

    private List<TodoCiUserGroupDetailVO> todoCiUserGroupDetailList;

    private List<TodoSystemAuthDetailDO> todoSystemAuthDetailList;

    private List<TodoBuilderPlanDetailDO> todoBuilderPlanDetailList;

    private List<TodoNewServerDetailDO> todoNewServerDetailList;

    private TodoSystemAuthDetailDO todoSystemAuthGetway;

    private TodoVpnDetailVO todoVpn;

    private HashMap<String, Object> newProjectMap;

    private HashMap<String, Object> cmdbRoleMap;

    private HashMap<String, Object> scmMap;

    private HashMap<String, Object> tomcatVersionMap;

    private TodoDO todoDO;

    private long id;

    //发起人userId
    private long initiatorUserId;

    //发起人username
    private String initiatorUsername;

    //发起人详细信息
    private UserDO initiatorUserDO;

    //工单id
    private long todoId;

    //负责人userId
    private long assigneeUserId;

    //负责人username
    private String assigneeUsername;

    //审批人userId
    private long approvalUserId;

    //审批人username
    private String approvalUsername;

    //工单状态
    private int todoStatus = 0;

    //历时显示
    private String timeView;

    //工单详情
    private String content = "";

    private String gmtCreate;

    private String gmtModify;

    @Override
    public String toString() {
        return "TodoDetailDO{" +
                "id=" + id +
                ", initiatorUserId=" + initiatorUserId +
                ", initiatorUsername='" + initiatorUsername + '\'' +
                ", todoId=" + todoId +
                ", assigneeUserId=" + assigneeUserId +
                ", assigneeUsername='" + assigneeUsername + '\'' +
                ", approvalUserId=" + approvalUserId +
                ", approvalUsername='" + approvalUsername + '\'' +
                ", todoStatus=" + todoStatus +
                ", content='" + content + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public List<UserDO> getAssigneeUsers() {
        return assigneeUsers;
    }

    public void setAssigneeUsers(List<UserDO> assigneeUsers) {
        this.assigneeUsers = assigneeUsers;
    }

    public List<TodoKeyboxDetailVO> getTodoKeyboxDetailList() {
        return todoKeyboxDetailList;
    }

    public List<TodoSystemAuthDetailDO> getTodoSystemAuthDetailList() {
        return todoSystemAuthDetailList;
    }

    public void setTodoSystemAuthDetailList(List<TodoSystemAuthDetailDO> todoSystemAuthDetailList) {
        this.todoSystemAuthDetailList = todoSystemAuthDetailList;
    }

    public void setTodoKeyboxDetailList(List<TodoKeyboxDetailVO> todoKeyboxDetailList) {
        this.todoKeyboxDetailList = todoKeyboxDetailList;
    }

    public TodoSystemAuthDetailDO getTodoSystemAuthGetway() {
        return todoSystemAuthGetway;
    }

    public void setTodoSystemAuthGetway(TodoSystemAuthDetailDO todoSystemAuthGetway) {
        this.todoSystemAuthGetway = todoSystemAuthGetway;
    }

    public TodoVpnDetailDO getTodoVpn() {
        return todoVpn;
    }

    public void setTodoVpn(TodoVpnDetailVO todoVpn) {
        this.todoVpn = todoVpn;
    }

    public UserDO getAssigneeUserDO() {
        return assigneeUserDO;
    }

    public void setAssigneeUserDO(UserDO assigneeUserDO) {
        this.assigneeUserDO = assigneeUserDO;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public TodoDO getTodoDO() {
        return todoDO;
    }

    public UserDO getInitiatorUserDO() {
        return initiatorUserDO;
    }

    public String getTimeView() {
        return timeView;
    }

    public void setTimeView(String timeView) {
        this.timeView = timeView;
    }

    public void setInitiatorUserDO(UserDO initiatorUserDO) {
        this.initiatorUserDO = initiatorUserDO;
    }

    public void setTodoDO(TodoDO todoDO) {
        this.todoDO = todoDO;
    }

    public long getInitiatorUserId() {
        return initiatorUserId;
    }

    public void setInitiatorUserId(long initiatorUserId) {
        this.initiatorUserId = initiatorUserId;
    }

    public String getInitiatorUsername() {
        return initiatorUsername;
    }

    public void setInitiatorUsername(String initiatorUsername) {
        this.initiatorUsername = initiatorUsername;
    }

    public long getTodoId() {
        return todoId;
    }

    public void setTodoId(long todoId) {
        this.todoId = todoId;
    }

    public long getAssigneeUserId() {
        return assigneeUserId;
    }

    public void setAssigneeUserId(long assigneeUserId) {
        this.assigneeUserId = assigneeUserId;
    }

    public String getAssigneeUsername() {
        return assigneeUsername;
    }

    public void setAssigneeUsername(String assigneeUsername) {
        this.assigneeUsername = assigneeUsername;
    }

    public long getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(long approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getApprovalUsername() {
        return approvalUsername;
    }

    public void setApprovalUsername(String approvalUsername) {
        this.approvalUsername = approvalUsername;
    }

    public int getTodoStatus() {
        return todoStatus;
    }

    public void setTodoStatus(int todoStatus) {
        this.todoStatus = todoStatus;
    }

    public HashMap<String, Object> getNewProjectMap() {
        return newProjectMap;
    }

    public void setNewProjectMap(HashMap<String, Object> newProjectMap) {
        this.newProjectMap = newProjectMap;
    }

    public List<TodoBuilderPlanDetailDO> getTodoBuilderPlanDetailList() {
        return todoBuilderPlanDetailList;
    }

    public void setTodoBuilderPlanDetailList(List<TodoBuilderPlanDetailDO> todoBuilderPlanDetailList) {
        this.todoBuilderPlanDetailList = todoBuilderPlanDetailList;
    }

    public List<TodoNewServerDetailDO> getTodoNewServerDetailList() {
        return todoNewServerDetailList;
    }

    public void setTodoNewServerDetailList(List<TodoNewServerDetailDO> todoNewServerDetailList) {
        this.todoNewServerDetailList = todoNewServerDetailList;
    }

    public List<TodoCiUserGroupDetailVO> getTodoCiUserGroupDetailList() {
        return todoCiUserGroupDetailList;
    }

    public void setTodoCiUserGroupDetailList(List<TodoCiUserGroupDetailVO> todoCiUserGroupDetailList) {
        this.todoCiUserGroupDetailList = todoCiUserGroupDetailList;
    }

    public HashMap<String, Object> getScmMap() {
        return scmMap;
    }

    public void setScmMap(HashMap<String, Object> scmMap) {
        this.scmMap = scmMap;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public HashMap<String, Object> getCmdbRoleMap() {
        return cmdbRoleMap;
    }

    public void setCmdbRoleMap(HashMap<String, Object> cmdbRoleMap) {
        this.cmdbRoleMap = cmdbRoleMap;
    }

    public HashMap<String, Object> getTomcatVersionMap() {
        return tomcatVersionMap;
    }

    public void setTomcatVersionMap(HashMap<String, Object> tomcatVersionMap) {
        this.tomcatVersionMap = tomcatVersionMap;
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
