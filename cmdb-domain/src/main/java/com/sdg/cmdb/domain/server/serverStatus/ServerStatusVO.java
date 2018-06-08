package com.sdg.cmdb.domain.server.serverStatus;

import java.io.Serializable;
import java.util.List;

public class ServerStatusVO implements Serializable {
    private static final long serialVersionUID = 1971099943055308059L;

    private List<ServerEnvTypeVO> serverEnvTypeList;

    private List<ServerTypeVO> serverTypeList;

    List<ServerCreateByMonthVO> serverCreateList;

    public List<ServerEnvTypeVO> getServerEnvTypeList() {
        return serverEnvTypeList;
    }

    public void setServerEnvTypeList(List<ServerEnvTypeVO> serverEnvTypeList) {
        this.serverEnvTypeList = serverEnvTypeList;
    }

    public List<ServerTypeVO> getServerTypeList() {
        return serverTypeList;
    }

    public void setServerTypeList(List<ServerTypeVO> serverTypeList) {
        this.serverTypeList = serverTypeList;
    }

    public List<ServerCreateByMonthVO> getServerCreateList() {
        return serverCreateList;
    }

    public void setServerCreateList(List<ServerCreateByMonthVO> serverCreateList) {
        this.serverCreateList = serverCreateList;
    }
}
