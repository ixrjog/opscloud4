package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.dao.cmdb.UserDao;
import com.sdg.cmdb.domain.ci.CiDeployServerVersionDO;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.DingtalkItemEnum;
import com.sdg.cmdb.domain.dingtalk.DingTalkContent;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksVO;
import com.sdg.cmdb.domain.jenkins.*;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import com.sdg.cmdb.handler.DingTalkHandler;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.DingtalkService;
import com.sdg.cmdb.service.ServerService;
import com.sdg.cmdb.service.control.configurationfile.AnsibleHostService;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrSubstitutor;
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
    private AnsibleHostService ansibleHostService;

    @Resource
    private CiDao ciDao;

    @Resource
    private ConfigCenterService configCenterService;

    @Resource
    private DingTalkHandler dingTalkHandler;

    private HashMap<String, String> configMap;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.DINGTALK.getItemKey());
    }

    /**
     * 后端部署webHook
     *
     * @return
     */
    private String acqWebhookToken() {
        HashMap<String, String> configMap = acqConifMap();
        String webhook = configMap.get(DingtalkItemEnum.DINGTALK_WEBHOOK.getItemKey());
        String token = configMap.get(DingtalkItemEnum.DINGTALK_TOKEN_DEPLOY.getItemKey());
        return webhook + token;
    }

    /**
     * 前端构建webHook
     *
     * @return
     */
    private String acqFtWebhookToken() {
        HashMap<String, String> configMap = acqConifMap();
        String webhook = configMap.get(DingtalkItemEnum.DINGTALK_WEBHOOK.getItemKey());
        String token = configMap.get(DingtalkItemEnum.DINGTALK_TOKEN_FT_BUILD.getItemKey());
        return webhook + token;
    }

    private String acqAndroidWebhookToken() {
        HashMap<String, String> configMap = acqConifMap();
        String webhook = configMap.get(DingtalkItemEnum.DINGTALK_WEBHOOK.getItemKey());
        String token = configMap.get(DingtalkItemEnum.DINGTALK_TOKEN_ANDROID_BUILD.getItemKey());
        return webhook + token;
    }

    private String acqIosWebhookToken() {
        HashMap<String, String> configMap = acqConifMap();
        String webhook = configMap.get(DingtalkItemEnum.DINGTALK_WEBHOOK.getItemKey());
        String token = configMap.get(DingtalkItemEnum.DINGTALK_TOKEN_IOS_BUILD.getItemKey());
        return webhook + token;
    }


    @Override
    public boolean sendCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) throws Exception {
        if (!isNotifyGray && ciDeployStatisticsDO.getEnv().equals(ServerDO.EnvTypeEnum.gray.getDesc())) return false;
        DingTalkContent dingTalkContent = new DingTalkContent();
        dingTalkContent.setWebHook(acqWebhookToken());
        dingTalkContent.setMsg(acqCiDeployMsg(ciDeployStatisticsDO));
        return dingTalkHandler.doNotify(dingTalkContent);
    }

    @Override
    public boolean sendFtBuildMsg(int envType, HashMap<String, String> params, JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        values.put("jobName", jobBuildDO.getJobName());
        values.put("buildType", GitlabWebHooksDO.HooksTypeEnum.getHooksTypeName(jobBuildDO.getBuildType()));
        // values.put("buildPhase", jobNoteVO.getBuildPhase());
        values.put("buildStatus", jobNoteVO.getBuildStatus());
        values.put("buildNumber", String.valueOf(jobBuildDO.getBuildNumber()));
        values.put("branch", String.valueOf(jobNoteVO.getBranch()));
        values.put("commit", jobNoteVO.getCommit().substring(0, 8));
        values.put("consoleUrl", jobNoteVO.getBuildConsoleUrl());
        values.put("username", jobBuildDO.getUsername());
        values.put("email", jobBuildDO.getEmail());
        values.put("mobile", jobBuildDO.getMobile());

        String deployPath = params.get("deployPath");

        // daily
        String envDesc = GitlabWebHooksVO.EnvTypeEnum.getEnvTypeName(envType);
        values.put("envDesc", envDesc);
        if (envType == GitlabWebHooksVO.EnvTypeEnum.header.getCode()) {

        }

        String dingtalkTemplate = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"前端构建消息\"," +
                "\"text\": \"### ${jobName}\\n";
        String buildPhase = jobNoteVO.getBuildPhase();
        values.put("buildPhase", buildPhase);
        if (buildPhase.equalsIgnoreCase("FINALIZED")) {
            dingtalkTemplate += "![](http://app.ci.51xianqu.net/job/" + jobBuildDO.getJobName() + "/badge/icon)\\n\\n";
        }
        dingtalkTemplate += "任务阶段 : ${buildPhase}\\n\\n";

        if (!StringUtils.isEmpty(jobNoteVO.getBuildStatus()))
            dingtalkTemplate += "状态 : ${buildStatus}\\n\\n";
        dingtalkTemplate += "构建编号 : ${buildNumber}\\n\\n" +
                "任务环境 : ${envDesc}\\n\\n" +
                "分支 : ${branch}\\n\\n" +
                "Commit : ${commit}\\n\\n" +
                "操作人 : ${username}\\n\\n";
        if (!StringUtils.isEmpty(jobBuildDO.getEmail()))
            dingtalkTemplate += "Email : ${email}\\n\\n";
        // 任务完成线上部署路径
        if (jobNoteVO.getBuildPhase().equalsIgnoreCase("FINALIZED")) {
            String url = "";
            if (envType == GitlabWebHooksVO.EnvTypeEnum.header.getCode()) {
               // url = deployPath.replace("img0-cdn","http://cdndaily.52shangou.com");
                url = "http://cdndaily.52shangou.com/" + deployPath;
            }
            if (envType == GitlabWebHooksVO.EnvTypeEnum.tag.getCode()) {
                url = deployPath.replace("img0-cdn","http://cdn.52shangou.com");
                //url = "http://cdn.52shangou.com/" + deployPath;
            }
            values.put("url", url);
            dingtalkTemplate += "url : ${url}\\n\\n";
        }

        dingtalkTemplate += "[构建日志](${consoleUrl})\\n\\n" +
                "\"}," +
                "\"at\": { \"atMobiles\": [\"${mobile}\"]," +
                "\"isAtAll\": false" +
                "}" +
                "}";
        StrSubstitutor sub = new StrSubstitutor(values);
        String resolvedString = sub.replace(dingtalkTemplate);
        DingTalkContent dingTalkContent = new DingTalkContent();
        dingTalkContent.setWebHook(acqFtWebhookToken());
        //System.err.println(resolvedString);
        dingTalkContent.setMsg(resolvedString);
        return dingTalkHandler.doNotify(dingTalkContent);
    }

    @Override
    public boolean sendAndroidBuildMsg(List<BuildArtifactDO> artifacts, HashMap<String, String> params, JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception {
        // img0-cdn
        Map<String, String> values = new HashMap<String, String>();
        values.put("jobName", jobBuildDO.getJobName());
        // 此buildType为用户定义
        //values.put("buildPhase", jobNoteVO.getBuildPhase());
        values.put("buildNumber", String.valueOf(jobBuildDO.getBuildNumber()));
        values.put("buildType", params.get("buildType"));
        if(params.containsKey("mbranch")){
            values.put("branch", params.get("mbranch"));
        }else{
            values.put("branch",  String.valueOf(jobNoteVO.getBranch()));
        }
        // String deployPath = params.get("deployPath");

        // values.put("deployPath",deployPath);
        //values.put("commit", jobNoteVO.getCommit().substring(0,8));
        values.put("consoleUrl", jobNoteVO.getBuildConsoleUrl());
        values.put("username", jobBuildDO.getUsername());
        values.put("email", jobBuildDO.getEmail());
        values.put("mobile", jobBuildDO.getMobile());
        String dingtalkTemplate = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"Android构建消息\"," +
                "\"text\": \"### ${jobName}\\n";
        String buildPhase = jobNoteVO.getBuildPhase();
        values.put("buildPhase", buildPhase);
        if (buildPhase.equalsIgnoreCase("FINALIZED")) {
            dingtalkTemplate += "![](http://app.ci.51xianqu.net/job/" + jobBuildDO.getJobName() + "/badge/icon)\\n\\n";
        }
        dingtalkTemplate += "任务阶段 : ${buildPhase}\\n\\n";
        String buildStatus = jobNoteVO.getBuildStatus();
        if (!StringUtils.isEmpty(buildStatus)) {
            values.put("buildStatus", jobNoteVO.getBuildStatus());
            dingtalkTemplate += "状态 : ${buildStatus}\\n\\n";
        }
        dingtalkTemplate += "构建编号 : ${buildNumber}\\n\\n" +
                "分支 : ${branch}\\n\\n" +
                "buildType : ${buildType}\\n\\n" +

                "操作人 : ${username}\\n\\n" +
                "Email : ${email}\\n\\n";

        String deployPath = params.get("deployPath");
        String uri = deployPath.replace("img0-cdn", "");
        for (BuildArtifactDO artifact : artifacts) {
            try {
                String r[] = artifact.getArtifactsName().split("/");
                String artifactName = r[r.length - 1];

                dingtalkTemplate += "url : http://cdn.52shangou.com" + uri + "/" + artifactName + "\\n\\n";
                dingtalkTemplate += "[" + artifactName + "](" + artifact.getArchiveUrl() + ")\\n\\n";
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        dingtalkTemplate += "[下载页面](https://work.52shangou.com/#/access/artifacts?id=" + jobBuildDO.getId() + ")\\n\\n";
        dingtalkTemplate += "[构建日志](${consoleUrl})\\n\\n" +
                "\"}," +
                "\"at\": { \"atMobiles\": [\"${mobile}\"]," +
                "\"isAtAll\": false" +
                "}" +
                "}";
        StrSubstitutor sub = new StrSubstitutor(values);
        String resolvedString = sub.replace(dingtalkTemplate);
        DingTalkContent dingTalkContent = new DingTalkContent();
        dingTalkContent.setWebHook(acqAndroidWebhookToken());
        dingTalkContent.setMsg(resolvedString);
        return dingTalkHandler.doNotify(dingTalkContent);
    }

    @Override
    public boolean sendIosBuildMsg(List<BuildArtifactDO> artifacts, HashMap<String, String> params, JenkinsJobBuildDO jobBuildDO, JobNoteVO jobNoteVO) throws Exception {
        Map<String, String> values = new HashMap<String, String>();
        values.put("jobName", jobBuildDO.getJobName());
        // 此buildType为用户定义
        String buildPhase = jobNoteVO.getBuildPhase();
        //values.put("buildPhase", buildPhase);
        values.put("buildNumber", String.valueOf(jobBuildDO.getBuildNumber()));
        values.put("buildType", params.get("buildType"));
        values.put("branch", params.get("mbranch"));
        // String deployPath = params.get("deployPath");
        // values.put("deployPath",deployPath);
        //values.put("commit", jobNoteVO.getCommit().substring(0,8));
        values.put("consoleUrl", jobNoteVO.getBuildConsoleUrl());
        values.put("username", jobBuildDO.getUsername());
        values.put("email", jobBuildDO.getEmail());
        values.put("mobile", jobBuildDO.getMobile());
        // e.g. img0-cdn/ci/ios/lhapprn/2017_12/master_daily_1228_235222
        String deployPath = params.get("deployPath");
        String uri = deployPath.replace("img0-cdn", "http://cdn.52shangou.com");
        String packageName = params.get("packageName");
        String dingtalkTemplate = "{ " +
                "\"msgtype\": \"markdown\", " +
                "\"markdown\": {" +
                "\"title\": \"iOS构建消息\"," +
                "\"text\": \"### ${jobName}\\n";

        values.put("buildPhase", buildPhase);
        if (buildPhase.equalsIgnoreCase("FINALIZED")) {
            dingtalkTemplate += "![](http://app.ci.51xianqu.net/job/" + jobBuildDO.getJobName() + "/badge/icon)\\n\\n";
        }
        dingtalkTemplate += "任务阶段 : ${buildPhase}\\n\\n";
        String buildStatus = jobNoteVO.getBuildStatus();
        if (!StringUtils.isEmpty(buildStatus)) {
            values.put("buildStatus", jobNoteVO.getBuildStatus());
            dingtalkTemplate += "状态 : ${buildStatus}\\n\\n";
        }

        dingtalkTemplate += "构建编号 : ${buildNumber}\\n\\n" +
                "分支 : ${branch}\\n\\n" +
                "buildType : ${buildType}\\n\\n" +
                "操作人 : ${username}\\n\\n" +
                "Email : ${email}\\n\\n";
        // 任务完成 插入文件下载路径
        if (buildStatus.equalsIgnoreCase("SUCCESS") && buildPhase.equalsIgnoreCase("FINALIZED")) {
            dingtalkTemplate += "url : " + uri + "/" + packageName + "\\n\\n";
            dingtalkTemplate += "[" + packageName + "](" + uri + "/" + packageName + ")\\n\\n";
            dingtalkTemplate += "url : " + uri + "/package.plist\\n\\n";
            dingtalkTemplate += "[package.plist](" + uri + "/package.plist)\\n\\n";
        }
        dingtalkTemplate += "[下载页面](https://work.52shangou.com/#/access/artifacts?id=" + jobBuildDO.getId() + ")\\n\\n";
        dingtalkTemplate += "[构建日志](${consoleUrl})\\n\\n" +
                "\"}," +
                "\"at\": { \"atMobiles\": [\"${mobile}\"]," +
                "\"isAtAll\": false" +
                "}" +
                "}";
        StrSubstitutor sub = new StrSubstitutor(values);
        String resolvedString = sub.replace(dingtalkTemplate);
        DingTalkContent dingTalkContent = new DingTalkContent();
        dingTalkContent.setWebHook(acqIosWebhookToken());
        dingTalkContent.setMsg(resolvedString);
        return dingTalkHandler.doNotify(dingTalkContent);
    }


    private String acqCiDeployMsg(CiDeployStatisticsDO ciDeployStatisticsDO) {
        String user = ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName() + "(" + userDao.getDisplayNameByUserName(ciDeployStatisticsDO.getBambooManualBuildTriggerReasonUserName()) + ")";
        ServerGroupDO serverGroupDO = serverGroupDao.queryServerGroupByName("group_" + ciDeployStatisticsDO.getProjectName());

        List<ServerDO> servers = ansibleHostService.queryServerGroupByCiGroupName(serverGroupDO, ciDeployStatisticsDO.getGroupName());
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
                    CiDeployServerVersionDO ciDeployServerVersionDO = ciDao.getCiDeployServerVersionByServerId(serverDO.getId());
                    if (ciDeployServerVersionDO != null) {
                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + acqServerDeployVersion(ciDeployStatisticsDO, ciDeployServerVersionDO) + "\\n";
                    } else {
                        otherServerMsg += "- " + serverDO.acqServerName() + " " + serverDO.getInsideIp() + " 无版本记录" + "\\n";
                    }
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
        CiDeployStatisticsDO serverDeployDO = ciDao.getCiDeployStatisticsById(ciDeployServerVersionDO.getCiDeployStatisticsId());
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


}
