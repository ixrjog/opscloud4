package com.sdg.cmdb.domain.task;

import com.sdg.cmdb.domain.server.ServerVO;

import java.io.Serializable;
import java.util.List;

public class CmdVO implements Serializable {
    private static final long serialVersionUID = 4792402825735723243L;


    private String hostGroup;

    private String cmd;

    private long taskScriptId;

    private String params;

    private List<ServerVO> serverList;

    public String getHostGroup() {
        return hostGroup;
    }

    public void setHostGroup(String hostGroup) {
        this.hostGroup = hostGroup;
    }

    public String getCmd() {
        return cmd;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public List<ServerVO> getServerList() {
        return serverList;
    }

    public void setServerList(List<ServerVO> serverList) {
        this.serverList = serverList;
    }

    public long getTaskScriptId() {
        return taskScriptId;
    }

    public void setTaskScriptId(long taskScriptId) {
        this.taskScriptId = taskScriptId;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }
}
