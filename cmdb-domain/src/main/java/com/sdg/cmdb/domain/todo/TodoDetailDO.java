package com.sdg.cmdb.domain.todo;

import com.sdg.cmdb.domain.auth.UserDO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/9/6.
 */
public class TodoDetailDO implements Serializable {
    private static final long serialVersionUID = -9208675335761959297L;

    private long id;

    //发起人userId
    private long initiatorUserId;

    //发起人username
    private String initiatorUsername;

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
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }


    public enum TodoStatusEnum {
        establish(0, "创建工单"),
        submit(1, "提交工单"),
        approval(2, "审批完成"),
        revoke(3, "撤销工单"),
        complete(9, "完成");
        private int code;
        private String desc;

        TodoStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getTodoStatusName(int code) {
            for (TodoStatusEnum todoStatusEnum : TodoStatusEnum.values()) {
                if (todoStatusEnum.getCode() == code) {
                    return todoStatusEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public enum ProcessStatusEnum {
        def(0, "默认"),
        complete(1, "处理完成"),
        err(2, "审批完成");

        private int code;
        private String desc;

        ProcessStatusEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getProcessStatusName(int code) {
            for (ProcessStatusEnum processStatusEnum : ProcessStatusEnum.values()) {
                if (processStatusEnum.getCode() == code) {
                    return processStatusEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public TodoDetailDO() {

    }

    public TodoDetailDO(UserDO userDO, long todoId) {
        this.initiatorUserId = userDO.getId();
        this.initiatorUsername = userDO.getUsername();
        this.todoId = todoId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
