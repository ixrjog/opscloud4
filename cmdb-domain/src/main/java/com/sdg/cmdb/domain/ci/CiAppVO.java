package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.dingtalk.DingtalkDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import lombok.Data;
import org.gitlab.api.models.GitlabBranch;

import java.io.Serializable;
import java.util.List;

@Data
public class CiAppVO extends CiAppDO implements Serializable {
    private static final long serialVersionUID = -1669568587783712390L;

    private List<GitlabBranch> branchList;
    private ServerGroupDO serverGroupDO;
    private DingtalkDO dingtalkDO;
    private List<DingtalkDO> dingtalkList;
    private List<UserVO> authUserList; // 授权用户

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
        setGitFlow(ciAppDO.isGitFlow());
        this.branchList = branchList;
        this.serverGroupDO = serverGroupDO;
    }

    public CiAppVO() {
    }

}
