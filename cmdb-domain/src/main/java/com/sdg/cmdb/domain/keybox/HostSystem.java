package com.sdg.cmdb.domain.keybox;

import java.io.Serializable;

/**
 * Created by zxxiao on 2016/11/4.
 */
public class HostSystem implements Serializable {
    private static final long serialVersionUID = -7732247515848169602L;

    public static final String INITIAL_STATUS="INITIAL";
    public static final String AUTH_FAIL_STATUS="AUTHFAIL";
    public static final String PUBLIC_KEY_FAIL_STATUS="KEYAUTHFAIL";
    public static final String GENERIC_FAIL_STATUS="GENERICFAIL";
    public static final String SUCCESS_STATUS="SUCCESS";
    public static final String HOST_FAIL_STATUS="HOSTFAIL";


    @Override
    public String toString() {
        return "HostSystem{" +

                "user='" + user + '\'' +
                ", host='" +  host + '\'' +
                ", port='" + port + '\'' +
                ", errorMsg='" + errorMsg + '\'' +
                ", instanceId='" + instanceId + '\'' +
                '}';
    }

    private String user = "root";

    private String host;

    private Integer port = 22;

    private String errorMsg;

    private String instanceId;

    private String statusCd = INITIAL_STATUS;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getStatusCd() {
        return statusCd;
    }

    public void setStatusCd(String statusCd) {
        this.statusCd = statusCd;
    }
}
