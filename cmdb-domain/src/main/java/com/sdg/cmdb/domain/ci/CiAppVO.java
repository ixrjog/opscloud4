package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.dingtalk.DingtalkDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.gitlab.api.models.GitlabBranch;

import java.io.Serializable;
import java.util.List;

public class CiAppVO extends CiAppDO implements Serializable {
    private static final long serialVersionUID = -1669568587783712390L;

    private List<GitlabBranch> branchList;

    private ServerGroupDO serverGroupDO;

    private DingtalkDO dingtalkDO;

    private List<DingtalkDO> dingtalkList;

    public CiAppVO(CiAppDO ciAppDO, List<GitlabBranch> branchList, ServerGroupDO serverGroupDO) {
        setId(ciAppDO.getId());
        setAppName(ciAppDO.getAppName());
        setProjectName(ciAppDO.getProjectName());
        setServerGroupId(ciAppDO.getServerGroupId());
        setServerGroupName(ciAppDO.getServerGroupName());
        setContent(ciAppDO.getContent());
        setSshUrl(ciAppDO.getSshUrl());
        setWebUrl(ciAppDO.getWebUrl());
        setProjectId(ciAppDO.getProjectId());
        setProjectName(ciAppDO.getProjectName());
        setCiType(ciAppDO.getCiType());
        setAppType(ciAppDO.getAppType());
        setGmtCreate(ciAppDO.getGmtCreate());
        setGmtModify(ciAppDO.getGmtModify());
        setAuthBranch(ciAppDO.isAuthBranch());
        setDingtalkId(ciAppDO.getDingtalkId());
        this.branchList = branchList;
        this.serverGroupDO = serverGroupDO;

    }

    public CiAppVO() {

    }

    public DingtalkDO getDingtalkDO() {
        return dingtalkDO;
    }

    public void setDingtalkDO(DingtalkDO dingtalkDO) {
        this.dingtalkDO = dingtalkDO;
    }

    public ServerGroupDO getServerGroupDO() {
        return serverGroupDO;
    }

    public void setServerGroupDO(ServerGroupDO serverGroupDO) {
        this.serverGroupDO = serverGroupDO;
    }

    public List<GitlabBranch> getBranchList() {
        return branchList;
    }

    public void setBranchList(List<GitlabBranch> branchList) {
        this.branchList = branchList;
    }

    public List<DingtalkDO> getDingtalkList() {
        return dingtalkList;
    }

    public void setDingtalkList(List<DingtalkDO> dingtalkList) {
        this.dingtalkList = dingtalkList;
    }
}
