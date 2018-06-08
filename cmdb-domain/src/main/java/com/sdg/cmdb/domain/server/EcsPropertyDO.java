package com.sdg.cmdb.domain.server;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/6/15.
 */
public class EcsPropertyDO implements Serializable {
    private static final long serialVersionUID = -6995833572607248516L;

    private long id;

    private long serverId;

    private String instanceId;

    private int propertyType;

    private String propertyValue;

    private String propertyName;

    private String gmtCreate;

    private String gmtModify;

    public EcsPropertyDO() {

    }

    public EcsPropertyDO(EcsServerDO ecsServerDO, int propertyType, String value) {
        if (ecsServerDO.getServerId() != 0)
            this.serverId = ecsServerDO.getServerId();
        this.instanceId = ecsServerDO.getInstanceId();
        this.propertyType = propertyType;
        this.propertyValue = value;
        this.propertyName = PropertyTypeEnum.getPropertyTypeName(propertyType);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public int getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(int propertyType) {
        this.propertyType = propertyType;
    }

    public String getPropertyValue() {
        return propertyValue;
    }

    public void setPropertyValue(String propertyValue) {
        this.propertyValue = propertyValue;
    }

    public String getPropertyName() {
        return PropertyTypeEnum.getPropertyTypeName(propertyType);
    }

    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
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
        return "EcsPropertyDO{" +
                "id=" + id +
                ", serverId=" + serverId +
                ", instanceId='" + instanceId + '\'' +
                ", propertyType=" + propertyType +
                ", propertyValue='" + propertyValue + '\'' +
                ", propertyName='" + propertyName + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                ", gmtCreate='" + gmtCreate + '\'' +
                '}';
    }


    public enum PropertyTypeEnum {
        imageId(0, "imageId"),
        networkType(1, "networkType"),
        vpcId(2, "vpcId"),
        vswitchId(3, "vswitchId"),
        securityGroupId(4, "securityGroupId");

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


}
