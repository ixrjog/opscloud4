package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

@Data
public class WorkflowTodoUserDO implements Serializable {

    private static final long serialVersionUID = 2349909897311211918L;
    private long id;
    private long todoId;
    private int assigneeType; // 代理类型

    public enum AssigneeTypeEnum {
        cmo(0, "cmo"),
        teamleader(1, "teamleader"),
        deptLeader(2, "deptLeader"),
        ops(3, "ops");
        private int code;
        private String desc;

        AssigneeTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getAssigneeTypeName(int code) {
            for (AssigneeTypeEnum assigneeTypeEnum : AssigneeTypeEnum.values()) {
                if (assigneeTypeEnum.getCode() == code) {
                    return assigneeTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    private String assigneeDesc;
    private long userId;
    private String username;
    private String displayName;

    /**
     * approve/disapprove/refuse/delegate
     */
    private int evaluation = 0;

    public enum EvaluationTypeEnum {
        empty(0, "empty"),
        approve(1, "approve"),
        disapprove(2, "disapprove"),
        refuse(3, "refuse"),
        delegate(4, "delegate");
        private int code;
        private String desc;

        EvaluationTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getEvaluationTypeName(int code) {
            for (EvaluationTypeEnum evaluationTypeEnum : EvaluationTypeEnum.values()) {
                if (evaluationTypeEnum.getCode() == code) {
                    return evaluationTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    private String evaluationMsg;
    private String gmtCreate;
    private String gmtModify;

    public WorkflowTodoUserDO() {}

    public WorkflowTodoUserDO(long todoId, UserDO userDO, int assigneeType) {
        this.todoId = todoId;
        this.userId = userDO.getId();
        this.username = userDO.getUsername();
        if (!StringUtils.isEmpty(userDO.getDisplayName()))
            this.displayName = userDO.getDisplayName();
        this.assigneeType = assigneeType;
        this.assigneeDesc = AssigneeTypeEnum.getAssigneeTypeName(assigneeType);
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
