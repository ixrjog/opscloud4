package com.sdg.cmdb.domain.jenkins;

import java.io.Serializable;

/**
 * 项目基础参数
 */
public class BaseParamDO implements Serializable {
    private static final long serialVersionUID = -5887657236549048028L;

    private long id;

    private long projectId;

    private String paramName;

    private String paramValue;

    private int paramType;

    private String content;

    private String gmtCreate;

    private String gmtModify;

    public enum ParamTypeEnum {
        // 标准参数 直接继承不可修改
        inherit(0, "inherit"),
        // 可变参数
        changeable(1, "changeable");
        private int code;
        private String desc;

        ParamTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getParamTypeName(int code) {
            for (ParamTypeEnum paramTypeEnum : ParamTypeEnum.values()) {
                if (paramTypeEnum.getCode() == code) {
                    return paramTypeEnum.getDesc();
                }
            }
            return "undefined";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public String getParamName() {
        return paramName;
    }

    public void setParamName(String paramName) {
        this.paramName = paramName;
    }

    public String getParamValue() {
        return paramValue;
    }

    public void setParamValue(String paramValue) {
        this.paramValue = paramValue;
    }

    public int getParamType() {
        return paramType;
    }

    public void setParamType(int paramType) {
        this.paramType = paramType;
    }

    public String getContent() {
        return content;
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
