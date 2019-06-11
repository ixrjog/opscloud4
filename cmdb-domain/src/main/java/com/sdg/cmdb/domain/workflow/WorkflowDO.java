package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowDO implements Serializable {
    private static final long serialVersionUID = 1430354332190209812L;

    private long id;
    private long groupId;
    private String wfName;
    private String wfKey;
    private int wfStatus; // 工单状态 0正常 1暂时关闭 2开发中
    private String title;
    private String content;
    private String helpUrl;
    private String topics; // 主题（用于搜索）
    private int wfType; // 工单类型
    private boolean approval = false;    // 是否 审批
    private boolean qaApproval = false;  // 质量审批
    private boolean cmoApproval = false; // 配置管理员审批
    private boolean tlApproval = false;  // tl审批
    private boolean dlApproval = false;
    private boolean opsAudit = false;    // ops审核
    private String gmtCreate;

    private String gmtModify;

    public enum WfTypeEnum {
        operation(0, "operation");
        private int code;
        private String desc;

        WfTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getWfTypeName(int code) {
            for (WfTypeEnum wfTypeEnum : WfTypeEnum.values()) {
                if (wfTypeEnum.getCode() == code) {
                    return wfTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
