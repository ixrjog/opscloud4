package com.sdg.cmdb.domain.jenkins;


import java.io.Serializable;
import java.util.List;

public class JenkinsProjectsVO extends JenkinsProjectsDO implements Serializable {
    private static final long serialVersionUID = 2310025019877332985L;

    private List<JenkinsProjectsEnvVO> envs;

    private List<BaseParamDO> baseParams;

    public JenkinsProjectsVO() {

    }

    public JenkinsProjectsVO(JenkinsProjectsDO jenkinsProjectsDO, List<JenkinsProjectsEnvVO> envs, List<BaseParamDO> baseParams) {
        setId(jenkinsProjectsDO.getId());
        setProjectName(jenkinsProjectsDO.getProjectName());
        setRepositoryUrl(jenkinsProjectsDO.getRepositoryUrl());
        setBuildType(jenkinsProjectsDO.getBuildType());
        setGmtCreate(jenkinsProjectsDO.getGmtCreate());
        setGmtModify(jenkinsProjectsDO.getGmtModify());
        setContent(jenkinsProjectsDO.getContent());
        this.envs = envs;
        this.baseParams = baseParams;
    }

    public List<JenkinsProjectsEnvVO> getEnvs() {
        return envs;
    }

    public void setEnvs(List<JenkinsProjectsEnvVO> envs) {
        this.envs = envs;
    }

    public List<BaseParamDO> getBaseParams() {
        return baseParams;
    }

    public void setBaseParams(List<BaseParamDO> baseParams) {
        this.baseParams = baseParams;
    }
}
