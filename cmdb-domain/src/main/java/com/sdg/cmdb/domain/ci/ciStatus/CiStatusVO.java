package com.sdg.cmdb.domain.ci.ciStatus;

import java.io.Serializable;
import java.util.List;

public class CiStatusVO implements Serializable {

    private static final long serialVersionUID = -8075029320520871824L;

    private List<CiProjectVO> topProjectList;

    private List<CiDeployUserVO> topUserList;

    private List<CiRepoVO> topRepoList;

    private List<CiDeployVO> ciDeployList;

    // 持续集成项目总数
    private int projectCnt = 0;

    // 持续集成部署次数
    private int deployedCnt = 0;


    public List<CiProjectVO> getTopProjectList() {
        return topProjectList;
    }

    public void setTopProjectList(List<CiProjectVO> topProjectList) {
        this.topProjectList = topProjectList;
    }

    public List<CiDeployUserVO> getTopUserList() {
        return topUserList;
    }

    public void setTopUserList(List<CiDeployUserVO> topUserList) {
        this.topUserList = topUserList;
    }

    public List<CiRepoVO> getTopRepoList() {
        return topRepoList;
    }

    public void setTopRepoList(List<CiRepoVO> topRepoList) {
        this.topRepoList = topRepoList;
    }

    public List<CiDeployVO> getCiDeployList() {
        return ciDeployList;
    }

    public void setCiDeployList(List<CiDeployVO> ciDeployList) {
        this.ciDeployList = ciDeployList;
    }

    public int getProjectCnt() {
        return projectCnt;
    }

    public void setProjectCnt(int projectCnt) {
        this.projectCnt = projectCnt;
    }

    public int getDeployedCnt() {
        return deployedCnt;
    }

    public void setDeployedCnt(int deployedCnt) {
        this.deployedCnt = deployedCnt;
    }
}
