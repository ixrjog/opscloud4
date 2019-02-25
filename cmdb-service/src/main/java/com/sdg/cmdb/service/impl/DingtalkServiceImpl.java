package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.DingtalkDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.aliyunMQ.AliyunMqGroupUserVO;
import com.sdg.cmdb.domain.aliyunMQ.AliyunMqGroupVO;
import com.sdg.cmdb.domain.ci.*;
import com.sdg.cmdb.domain.dingtalk.DingTalkContent;
import com.sdg.cmdb.domain.dingtalk.DingtalkDO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.handler.DingTalkHandler;
import com.sdg.cmdb.service.*;
import com.sdg.cmdb.service.configurationProcessor.AnsibleFileProcessorService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by liangjian on 2017/4/27.
 */
@Service
public class DingtalkServiceImpl implements DingtalkService {

    private static final Logger logger = LoggerFactory.getLogger(DingtalkServiceImpl.class);

    /**
     * 灰度部署通知
     */
    private boolean isNotifyGray = false;

    @Resource
    private UserDao userDao;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private ServerService serverService;

    @Resource
    private AnsibleFileProcessorService ansibleFileProcessorService;

    @Resource
    private CiDao ciDao;

    @Resource
    private CiService ciService;

    @Resource
    private DingtalkDao dingtalkDao;

    @Resource
    private ConfigCenterService configCenterService;


    // TODO 构建图标网站 https://shields.io/#/examples/build
    public static final String BUILD_PASSING = "https://img.shields.io/badge/build-passing-brightgreen.jpg";
    public static final String BUILD_FAILING = "https://img.shields.io/badge/build-failing-red.jpg";

    public static final String DEPLOYMENT_PASSING = "https://img.shields.io/badge/deployment-passing-brightgreen.jpg";
    public static final String DEPLOYMENT_FAILING = "https://img.shields.io/badge/deployment-failing-red.jpg";

    @Resource
    private DingTalkHandler dingTalkHandler;




    private String acqCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) {
        String user = ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName() + "(" + userDao.getDisplayNameByUserName(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName()) + ")";
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + ciDeployStatisticsDO.getProjectName());

        List<ServerDO> servers = ansibleFileProcessorService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployStatisticsDO.getGroupName());
        String serversMsg = "";
        String otherServerMsg = "";
        if (servers.size() != 0) {
            for (ServerDO serverDO : servers) {
                serversMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + "\\n";
            }

            List<ServerDO> otherServers = serverService.acqOtherServers(servers);
            if (otherServers.size() != 0) {
                otherServerMsg += "\\n\\n其它服务器版本信息 :" + " \\n";
                otherServerMsg += "> ";
                for (ServerDO serverDO : otherServers) {
                    // CiDeployServerVersionDO ciDeployServerVersionDO = ciDao.getCiDeployServerVersionByServerId(serverDO.getId());
//                    if (ciDeployServerVersionDO != null) {
//                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + acqServerDeployVersion(ciDeployStatisticsDO, ciDeployServerVersionDO) + "\\n";
//                    } else {
//                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + " 无版本记录" + "\\n";
//                    }
                }
            }
        }

        String textMsg = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"部署消息\"," +
                "\"text\": \"### " + ciDeployStatisticsDO.getProjectName() + "\\n" +
                "环境 : " + ciDeployStatisticsDO.getEnv() + "\\n\\n" +
                "版本名称 : " + ciDeployStatisticsDO.getBambooDeployVersion() + "\\n\\n" +
                "构建编号 : " + ciDeployStatisticsDO.getBambooBuildNumber() + "\\n\\n" +
                "分支 : " + ciDeployStatisticsDO.getBranchName() + "\\n\\n" +
                "操作人 : " + user + "\\n\\n" +
                "服务器信息 : \\n\\n" +
                "> 当前部署的服务器组 : " + ciDeployStatisticsDO.getGroupName() + " \\n" +
                serversMsg + " \\n" +
                otherServerMsg + " \\n" +
                "\"}" +
                "}";
        return textMsg;
    }

    /**
     * 获取服务器的部署版本信息
     *
     * @param ciDeployServerVersionDO
     * @return
     */
    private String acqServerDeployVersion(CiDeployStatisticsDO ciDeployStatisticsDO, CiDeployServerVersionDO ciDeployServerVersionDO) {
        // CiDeployStatisticsDO serverDeployDO = ciDao.getCiDeployStatisticsById(ciDeployServerVersionDO.getCiDeployStatisticsId());
        CiDeployStatisticsDO serverDeployDO = new CiDeployStatisticsDO();
        if (ciDeployStatisticsDO != null) {
            if (serverDeployDO.getBambooBuildNumber() == ciDeployStatisticsDO.getBambooBuildNumber()) {
                // 版本相同
                return " (版本相同)";
            } else {
                // 版本不同
                return "\\n\\n>版本信息 : " + serverDeployDO.getBambooDeployVersion() + "  " + serverDeployDO.getBambooBuildNumber() + "\\n\\n> 部署时间 : " + serverDeployDO.getGmtCreate();
            }
        } else {
            return "\\n无部署版本信息记录";
        }
    }


    /**
     * {
     * "msgtype": "text",
     * "text": {
     * "content": "我就是我,  @1825718XXXX 是不一样的烟火"
     * },
     * "at": {
     * "atMobiles": [
     * "1825718XXXX"
     * ],
     * "isAtAll": false
     * }
     * }
     *
     * @return
     * @throws Exception
     */
    @Override
    public void notifyCi(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO) {
        logger.info("Dingtalk build notify buiildId : {}", ciBuildDO.getId());
        // TODO 排除通知
        if (buildNotifyDO.getBuildPhase().equals("QUEUED")) return;
        if (buildNotifyDO.getBuildPhase().equals("COMPLETED")) return;
        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        // TODO 判断是否设置消息通知
        if (ciAppDO.getDingtalkId() == 0) return;

        List<ServerDO> serverList = ciService.getHostPattern(ciBuildDO, ciAppDO.getServerGroupId());
        String hostPattern = ciService.getBuildParamValue(ciBuildDO, CiServiceImpl.PARAM_HOSTPATTERN);

        int buildNumber = buildNotifyDO.getBuildNumber();

        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("projectName", ciAppDO.getProjectName());
        valuesMap.put("envName", ServerDO.EnvTypeEnum.getEnvTypeName(ciJobDO.getEnvType()));
        valuesMap.put("buildPhase", buildNotifyDO.getBuildPhase());
        String buildStatus = "";
        if (!StringUtils.isEmpty(buildNotifyDO.getBuildStatus()))
            buildStatus = "执行结果 : " + buildNotifyDO.getBuildStatus() + "\\n\\n";
        valuesMap.put("versionName", ciBuildDO.getVersionName());
        String versionDesc = "";
        if (!StringUtils.isEmpty(ciBuildDO.getVersionDesc()))
            versionDesc = "versionDesc : " + ciBuildDO.getVersionDesc() + "\\n\\n";

        valuesMap.put("versionDesc", ciBuildDO.getVersionDesc());
        valuesMap.put("jobName", ciBuildDO.getJobName());
        valuesMap.put("console", buildNotifyDO.getBuildFullUrl() + CiServiceImpl.BUILD_CONSOLE);
        valuesMap.put("buildNumber", buildNumber + "");
        valuesMap.put("username", ciBuildDO.getDisplayName());
        valuesMap.put("hostPattern", hostPattern);
        String branch = ciService.getBuildParamValue(ciBuildDO, CiServiceImpl.PARAM_BRANCH);
        valuesMap.put("branch", branch);
        valuesMap.put("commit", buildNotifyDO.getScmCommit().substring(0, 10));
        // https://oc.ops.yangege.cn/#/access/ciJob?appId=1&jobId=1
        valuesMap.put("ciJobUrl", "https://oc.ops.yangege.cn/#/access/ciJob?appId=" + ciAppDO.getId() + "&jobId=" + ciJobDO.getId());

        String buildFinalized = "";
        if (buildNotifyDO.getBuildPhase().equals("FINALIZED")) {
            if (buildNotifyDO.getBuildStatus().equals("SUCCESS")) {
                // 拆分
                if (ciAppDO.getCiType() == 0) {
                    buildFinalized = " ![](" + this.BUILD_PASSING + ")\\n\\n";
                } else {
                    buildFinalized = " ![](" + this.DEPLOYMENT_PASSING + ")\\n\\n";
                }
            } else {
                if (ciAppDO.getCiType() == 0) {
                    buildFinalized = " ![](" + this.BUILD_FAILING + ")\\n\\n";
                } else {
                    buildFinalized = " ![](" + this.DEPLOYMENT_FAILING + ")\\n\\n";
                }
            }
        }

        String templateString = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"部署消息\"," +
                "\"text\": \"### ${projectName}\\n" +
                buildFinalized +
                "环境 : ${envName}\\n\\n" +
                "任务名称 : ${jobName}\\n\\n" +
                "任务阶段 : ${buildPhase}\\n\\n" +

                "[控制台日志](${console})\\n\\n" +
                buildStatus +
                "版本名称 : ${versionName}\\n\\n" +
                versionDesc +
                "任务编号 : ${buildNumber}\\n\\n" +
                "分支 : ${branch}\\n\\n" +
                "COMMIT : ${commit}\\n\\n" +
                getCommitInfo(ciBuildDO.getId()) + "\\n" +
                "操作人 : ${username}\\n\\n" +
                "@" + ciBuildDO.getMobile() + "\\n\\n" +
                "主机组 : ${hostPattern} \\n\\n" +
                getServerDeployInfo(serverList) + "\\n" +
                getOtherServerDeployInfo(serverList, buildNumber) + "\\n\\n" +
                "[任务详情](${ciJobUrl})\\n" +
                "\"}" + getAt(ciBuildDO) +
                "}";

        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        System.err.println(resolvedString);

        try {
            if (ciAppDO.getDingtalkId() == 0) return;
            DingtalkDO dingtalkDO = dingtalkDao.getDingtalk(ciAppDO.getDingtalkId());

            DingTalkContent dingTalkContent = new DingTalkContent();
            dingTalkContent.setWebHook(dingtalkDO.getWebhook());
            dingTalkContent.setMsg(resolvedString);

            if (dingTalkHandler.doNotify(dingTalkContent)) ;
            buildNotifyDO.setDingtalk(true);
            ciDao.updateBuildNotify(buildNotifyDO);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String getServerDeployInfo(List<ServerDO> serverList) {
        String serversInfo = "";
        for (ServerDO serverDO : serverList) {
            serversInfo += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + "\\n";
        }
        // TODO 校验结果
        if (StringUtils.isEmpty(serversInfo))
            serversInfo = "无服务器信息，请联系管理员排查！";
        return serversInfo;
    }


    private String getOtherServerDeployInfo(List<ServerDO> serverList, int buildNumber) {
        List<ServerDO> otherServerList = serverService.acqOtherServers(serverList);
        String serversInfoHearder = "\\n\\n> 其它服务器版本信息:\\n\\n";
        String serversInfo = "";
        String gmtDeploy = "";
        for (ServerDO serverDO : otherServerList) {
            CiDeployDO ciDeployDO = ciDao.getDeployByServerId(serverDO.getId());
            if (ciDeployDO == null || ciDeployDO.getCiHistoryId() == 0) {
                serversInfo += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + " 无历史版本信息";
            } else {
                CiDeployHistoryDO ciDeployHistoryDO = ciDao.getDeployHistory(ciDeployDO.getCiHistoryId());
                if (ciDeployHistoryDO.getBuildNumber() != buildNumber) {
                    serversInfo += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + " " + ciDeployHistoryDO.getDeployVersionInfo() + "\\n";
                } else {
                    serversInfo += "- " + serverDO.acqServerName() + " 版本相同\\n";
                }
            }
            gmtDeploy = ciDeployDO.getGmtModify();
        }
        // TODO 校验结果
        if (!StringUtils.isEmpty(serversInfo))
            serversInfo = serversInfoHearder + serversInfo + "\\n\\n> 部署时间 : " + gmtDeploy;
        return serversInfo;
    }


    private String getCommitInfo(long buildId) {
        String commitInfoHeader = "变更记录:\\n\\n";
        String commitInfo = "";
        List<CiBuildCommitDO> commitList = ciDao.queryCiBuildCommitByBuildId(buildId);

        // List<GitlabCommit> commitList = gitlabService.getProjectChanges(projectName,branch, commitHash);
        for (CiBuildCommitDO ciBuildCommitDO : commitList) {
            commitInfo += "- " + ciBuildCommitDO.getMessage() + "\\n";
        }
        if (!StringUtils.isEmpty(commitInfo))
            commitInfo = commitInfoHeader + commitInfo;
        return commitInfo;
    }

    /**
     * ,
     * "at": {
     * "atMobiles": [
     * "1825718XXXX"
     * ],
     * "isAtAll": false
     * }
     *
     * @return
     */
    private String getAt(CiBuildDO ciBuildDO) {
        if (StringUtils.isEmpty(ciBuildDO.getMobile())) return "";
        String at = "," +
                "\"at\": {" +
                "\"atMobiles\": [\"" + ciBuildDO.getMobile() + "\"]" +
                "}";
        return at;
    }


    @Override
    public void notifyDeploy(CiBuildDO ciBuildDO, BuildNotifyDO buildNotifyDO) {
        logger.info("Dingtalk deploy notify buiildId : {}", ciBuildDO.getId());
        // TODO 排除通知
        if (buildNotifyDO.getBuildPhase().equals("QUEUED")) return;
        if (buildNotifyDO.getBuildPhase().equals("COMPLETED")) return;
        CiJobDO ciJobDO = ciDao.getCiJob(ciBuildDO.getJobId());
        CiAppDO ciAppDO = ciDao.getCiApp(ciJobDO.getAppId());
        // TODO 判断是否设置消息通知
        if (ciAppDO.getDingtalkId() == 0) {
            logger.info("未设置Dingtalk,通知不发送！");
            return;
        }

        List<ServerDO> serverList = ciService.getHostPattern(ciBuildDO, ciAppDO.getServerGroupId());
        String hostPattern = ciService.getBuildParamValue(ciBuildDO, CiServiceImpl.PARAM_HOSTPATTERN);
        String ossPath = ciService.getBuildParamValue(ciBuildDO, CiServiceImpl.PARAM_OSS_PATH);

        int buildNumber = buildNotifyDO.getBuildNumber();

        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("projectName", ciAppDO.getProjectName());
        valuesMap.put("envName", ServerDO.EnvTypeEnum.getEnvTypeName(ciJobDO.getEnvType()));
        valuesMap.put("buildPhase", buildNotifyDO.getBuildPhase());
        String buildStatus = "";
        if (!StringUtils.isEmpty(buildNotifyDO.getBuildStatus()))
            buildStatus = "执行结果 : " + buildNotifyDO.getBuildStatus() + "\\n\\n";
        valuesMap.put("versionName", ciBuildDO.getVersionName());
        String versionDesc = "";
        if (!StringUtils.isEmpty(ciBuildDO.getVersionDesc()))
            versionDesc = "versionDesc : " + ciBuildDO.getVersionDesc() + "\\n\\n";

        valuesMap.put("versionDesc", ciBuildDO.getVersionDesc());
        valuesMap.put("jobName", ciBuildDO.getJobName());
        valuesMap.put("console", buildNotifyDO.getBuildFullUrl() + CiServiceImpl.BUILD_CONSOLE);
        valuesMap.put("buildNumber", buildNumber + "");
        valuesMap.put("username", ciBuildDO.getDisplayName());
        valuesMap.put("hostPattern", hostPattern);
        valuesMap.put("ossPath", ossPath);
        // https://oc.ops.yangege.cn/#/access/ciJob?appId=1&jobId=1
        valuesMap.put("ciJobUrl", "https://oc.ops.yangege.cn/#/access/ciJobDeploy?appId=" + ciAppDO.getId() + "&jobId=" + ciJobDO.getId());

        String buildFinalized = "";
        if (buildNotifyDO.getBuildPhase().equals("FINALIZED")) {
            if (buildNotifyDO.getBuildStatus().equals("SUCCESS")) {
                // 拆分
                if (ciAppDO.getCiType() == 0) {
                    buildFinalized = " ![](" + this.BUILD_PASSING + ")\\n\\n";
                } else {
                    buildFinalized = " ![](" + this.DEPLOYMENT_PASSING + ")\\n\\n";
                }
            } else {
                if (ciAppDO.getCiType() == 0) {
                    buildFinalized = " ![](" + this.BUILD_FAILING + ")\\n\\n";
                } else {
                    buildFinalized = " ![](" + this.DEPLOYMENT_FAILING + ")\\n\\n";
                }
            }
        }

        String templateString = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"部署消息\"," +
                "\"text\": \"### ${projectName}\\n" +
                buildFinalized +
                "环境 : ${envName}\\n\\n" +
                "任务名称 : ${jobName}\\n\\n" +
                "任务阶段 : ${buildPhase}\\n\\n" +
                "[控制台日志](${console})\\n\\n" +
                buildStatus +
                "部署版本 : ${versionName}\\n\\n" +
                versionDesc +
                "任务编号 : ${buildNumber}\\n\\n" +
                "OSS路径 : ${ossPath}\\n\\n" +
                "操作人 : ${username}\\n\\n" +
                "@" + ciBuildDO.getMobile() + "\\n\\n" +
                "主机组 : ${hostPattern} \\n\\n" +
                getServerDeployInfo(serverList) + "\\n" +
                getOtherServerDeployInfo(serverList, buildNumber) + "\\n\\n" +
                "[任务详情](${ciJobUrl})\\n" +
                "\"}" + getAt(ciBuildDO) +
                "}";

        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        System.err.println(resolvedString);

        try {
            if (ciAppDO.getDingtalkId() == 0) return;
            DingtalkDO dingtalkDO = dingtalkDao.getDingtalk(ciAppDO.getDingtalkId());

            DingTalkContent dingTalkContent = new DingTalkContent();
            dingTalkContent.setWebHook(dingtalkDO.getWebhook());
            dingTalkContent.setMsg(resolvedString);

            if (dingTalkHandler.doNotify(dingTalkContent)) ;
            buildNotifyDO.setDingtalk(true);
            ciDao.updateBuildNotify(buildNotifyDO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void notifyMQAlarm(AliyunMqGroupVO aliyunMqGroupVO) {
        logger.info("Dingtalk MQ alarm notify!");
        Map<String, String> valuesMap = new HashMap<String, String>();

        valuesMap.put("groupId", aliyunMqGroupVO.getGroupId());
        valuesMap.put("totalDiff", aliyunMqGroupVO.getLastTotalDiff() + "/" + aliyunMqGroupVO.getTotalDiff());
        valuesMap.put("delayTime", aliyunMqGroupVO.getLastDelayTime() + "/" + aliyunMqGroupVO.getDelayTime());
        valuesMap.put("online", aliyunMqGroupVO.isLastOnline()+ "");
        valuesMap.put("rebalanceOK", aliyunMqGroupVO.isLastRebalanceOK() + "");

        String users = "";
        String mobiles = "";

        for (AliyunMqGroupUserVO user : aliyunMqGroupVO.getUserList()) {
            String mobile = user.getUserVO().getMobile();
            users += "@" + mobile + " ";
            if (!StringUtils.isEmpty(mobiles))
                mobiles += ",";
            mobiles += "\"" + mobile + "\"";
        }
        valuesMap.put("users", users);
        String at = "," +
                "\"at\": {" +
                "\"atMobiles\": [" + mobiles + "]" +
                "}";
        String templateString = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"阿里云MQ告警\"," +
                "\"text\": \"### 阿里云MQ告警\\n" +
                "${groupId}\\n\\n" +
                "堆积 : ${totalDiff}\\n\\n" +
                "延迟 : ${delayTime}\\n\\n" +
                "在线 : ${online}\\n\\n" +
                "一致 : ${rebalanceOK}\\n\\n" +
                "接收人 : ${users}\\n\\n" +
                "\"}" + at +
                "}";

        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        //System.err.println(resolvedString);
        try {
            DingtalkDO dingtalkDO = dingtalkDao.getDingtalkByType(1);
            DingTalkContent dingTalkContent = new DingTalkContent();
            dingTalkContent.setWebHook(dingtalkDO.getWebhook());
            dingTalkContent.setMsg(resolvedString);
            if (dingTalkHandler.doNotify(dingTalkContent)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    public void notifyTest() {
        logger.info("Dingtalk MQ alarm notify!");


        // https://img.shields.io/badge/MQ%E5%91%8A%E8%AD%A6-brightgreen.svg

        String dingtalkString = "{\n" +
                "    \"actionCard\": {\n" +
                "        \"title\": \"乔布斯 20 年前想打造一间苹果咖啡厅，而它正是 Apple Store 的前身\", \n" +
                "         \"text\": \"![screenshot](https://img.shields.io/badge/mq-brightgreen.jpg) \\n #### 乔布斯 20 年前想打造的苹果咖啡厅 \\n\\n Apple Store 的设计正从原来满满的科技感走向生活化，而其生活化的走向其实可以追溯到 20 年前苹果一个建立咖啡馆的计划\", \n" +
                "        \"hideAvatar\": \"0\", \n" +
                "        \"btnOrientation\": \"0\", \n" +
                "        \"singleTitle\" : \"详情\",\n" +
                "        \"singleURL\" : \"http://zabbix.ops.yangege.cn/\"\n" +
                "    }, \n" +
                "    \"msgtype\": \"actionCard\"\n" +
                "}";

        //System.err.println(resolvedString);
        try {
            DingtalkDO dingtalkDO = dingtalkDao.getDingtalk(1);
            DingTalkContent dingTalkContent = new DingTalkContent();
            dingTalkContent.setWebHook(dingtalkDO.getWebhook());
            dingTalkContent.setMsg( dingtalkString);
            if (dingTalkHandler.doNotify(dingTalkContent)) ;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
