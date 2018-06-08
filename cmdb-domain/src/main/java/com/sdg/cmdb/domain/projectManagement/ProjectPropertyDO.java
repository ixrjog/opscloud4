package com.sdg.cmdb.domain.projectManagement;

import java.io.Serializable;

public class ProjectPropertyDO implements Serializable {
    private static final long serialVersionUID = -2454527670253980300L;

    private long id;

    // ProjectManagementDO id
    private long pmId;

    // 0 userId
    // 1 serverGroupId
    private int propertyType;

    private long propertyValue;

    private String gmtCreate;

    private String gmtModify;


    public ProjectPropertyDO() {

    }

    public ProjectPropertyDO(long pmId, int propertyType, long propertyValue) {
        this.pmId = pmId;
        this.propertyType = propertyType;
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        return "ProjectPropertyDO{" +
                "id=" + id +
                ", pmId=" + pmId +
                ", propertyType=" + propertyType +
                ", propertyValue=" + propertyValue +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }

    public enum PropertyTypeEnum {
        //0 保留／在组中代表的是所有权限
        user(0, "user"),
        serverGroup(1, "serverGroup");
        private int code;
        private String desc;

        PropertyTypeEnum(int code, String desc) {
            this.code = code;
            this.desc = desc;
        }

        public int getCode() {
            return code;
        }

        public String getDesc() {
            return desc;
        }

        public static String getPropertyTypeName(int code) {
            for (PropertyTypeEnum propertyTypeEnum : PropertyTypeEnum.values()) {
                if (propertyTypeEnum.getCode() == code) {
                    return propertyTypeEnum.getDesc();
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

    public long getPmId() {
        return pmId;
    }

    public void setPmId(long pmId) {
        this.pmId = pmId;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public long getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(long propertyValue) {
        this.propertyValue = propertyValue;
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
