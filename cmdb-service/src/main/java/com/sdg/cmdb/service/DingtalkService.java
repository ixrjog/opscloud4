package com.sdg.cmdb.service;

import com.sdg.cmdb.domain.aliyunMQ.AliyunMqGroupVO;
import com.sdg.cmdb.domain.ci.BuildArtifactDO;
import com.sdg.cmdb.domain.ci.BuildNotifyDO;
import com.sdg.cmdb.domain.ci.CiBuildDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.jenkins.JenkinsJobBuildDO;
import com.sdg.cmdb.domain.jenkins.JobNoteVO;

import java.util.HashMap;
import java.util.List;

/**
 * Created by liangjian on 2017/4/27.
 */
public interface DingtalkService {


    /**
     * 持续集成通知
     * @param ciBuildDO
     * @param buildNotifyDO
     * @throws Exception
     */
    void notifyCi(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO);

    /**
     * 持续集成部署通知
     * @param ciBuildDO
     * @param buildNotifyDO
     */
    void notifyDeploy(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO);

    void notifyMQAlarm(AliyunMqGroupVO aliyunMqGroupVO);
}
