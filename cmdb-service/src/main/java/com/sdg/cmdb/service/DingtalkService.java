package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.jenkins.BuildArtifactDO;
import com.sdg.cmdb.domain.jenkins.JenkinsJobBuildDO;
import com.sdg.cmdb.domain.jenkins.JobNoteDO;
import com.sdg.cmdb.domain.jenkins.JobNoteVO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/4/27.
 */
public interface DingtalkService {


    /**
     * 部署消息通知-钉钉群
     *
     * @param ciDeployStatisticsDO
     * @throws Exception
     */
    boolean sendCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) throws Exception;


    /**
     * 前端部署消息通知-钉钉群
     *
     * @param jobBuildDO
     * @param jobNoteVO
     * @return
     * @throws Exception
     */
    boolean sendFtBuildMsg(int envType,HashMap<String, String> params,JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception;


    /**
     * android部署消息通知-钉钉群
     *
     * @param jobBuildDO
     * @param jobNoteVO
     * @return
     * @throws Exception
     */
    boolean sendAndroidBuildMsg(List<BuildArtifactDO> artifacts, HashMap<String, String> params, JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception;


    boolean sendIosBuildMsg(List<BuildArtifactDO> artifacts, HashMap<String, String> params, JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception;
}
