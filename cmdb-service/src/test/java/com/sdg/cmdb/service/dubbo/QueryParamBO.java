package com.sdg.cmdb.service.dubbo;

import lombok.Data;

import java.io.Serializable;

@Data
public class QueryParamBO implements Serializable {

    private static final long serialVersionUID = 3139450460518668039L;
    private String serviceInterface;
    private String methodName;

    private String serverIp;
    private String serverPort;

    public QueryParamBO(){

    }
    public QueryParamBO(String serviceInterface, String methodName, String serverIp, String serverPort) {
        this.serviceInterface = serviceInterface;
        this.methodName = methodName;
        this.serverIp = serverIp;
        this.serverPort = serverPort;

    }
}
