package com.sdg.cmdb.service.impl;

import com.sdg.cmdb.dao.cmdb.GitlabDao;
import com.sdg.cmdb.domain.BusinessWrapper;
import com.sdg.cmdb.domain.configCenter.ConfigCenterItemGroupEnum;
import com.sdg.cmdb.domain.configCenter.itemEnum.JenkinsItemEnum;
import com.sdg.cmdb.domain.gitlab.CommitsVO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksCommitsDO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksDO;
import com.sdg.cmdb.domain.gitlab.GitlabWebHooksVO;
import com.sdg.cmdb.service.ConfigCenterService;
import com.sdg.cmdb.service.GitlabService;
import com.sdg.cmdb.service.JenkinsService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.net.URL;
import java.util.HashMap;
import java.util.List;


@Service
public class GitlabServiceImpl implements GitlabService {

    // 分支类型
    private static final String GIT_TAG_PREFIX = "refs/tags/";
    private static final String GIT_HEADER_PREFIX = "refs/heads/";

    private static final String GIT_COMMIT_NULL ="0000000000000000000000000000000000000000";


    @Resource
    private GitlabDao gitlabDao;

    @Resource
    private JenkinsService jenkinsService;



    @Resource
    private ConfigCenterService configCenterService;

    private HashMap<String, String> configMap;

    @Value("#{cmdb['invoke.env']}")
    private String invokeEnv;

    private HashMap<String, String> acqConifMap() {
        if (configMap != null) return configMap;
        return configCenterService.getItemGroup(ConfigCenterItemGroupEnum.JENKINS.getItemKey());
    }

    /**
     * Git WehHooks接口（支持动态&静态路径部署）
     * @param webHooks
     * @return
     */
    @Override
    public BusinessWrapper<Boolean> webHooks(GitlabWebHooksVO webHooks) {
        String email = acqEmailByWebHooks(webHooks);
        GitlabWebHooksDO webHooksDO = new GitlabWebHooksDO(webHooks, acqProjectName(webHooks), GitlabWebHooksDO.HooksTypeEnum.ft.getCode(), email);
        // 插入触发code
        invokeFtBuild(webHooksDO);
        List<CommitsVO> commits = webHooks.getCommits();
        try {
            gitlabDao.addWebHooks(webHooksDO);
            if (commits != null) {
                for (CommitsVO commitsVO : commits) {
                    GitlabWebHooksCommitsDO commitsDO = new GitlabWebHooksCommitsDO(commitsVO, webHooksDO.getId());
                    gitlabDao.addCommits(commitsDO);
                }
            }
            // 执行构建
            if (webHooksDO.getTriggerBuild() == GitlabWebHooksDO.TriggerTypeEnum.trigger.getCode()) {
                boolean result = jenkinsService.buildFtJob(webHooksDO, acqParams(webHooksDO), acqFtEnvType(webHooksDO));
            }
            return new BusinessWrapper<Boolean>(true);
        } catch (Exception e) {
            e.printStackTrace();
            return new BusinessWrapper<Boolean>(false);
        }
    }

    private String acqEmailByCommit(String commit) {
        List<GitlabWebHooksCommitsDO> list = gitlabDao.queryCommitsByCommit(commit);
        for (GitlabWebHooksCommitsDO commitsDO : list) {
            if (!StringUtils.isEmpty(commitsDO.getAutherEmail()))
                return commitsDO.getAutherEmail();
        }
        return "";
    }

    @Override
    public String acqEmailByWebHooks(GitlabWebHooksVO webHooks) {
        String email = webHooks.acqEmail();
        if (StringUtils.isEmpty(email))
            return acqEmailByCommit(webHooks.getAfter());
        return email;
    }


    /**
     * 插入前端是否构建code
     *
     * @return
     */
    private void invokeFtBuild(GitlabWebHooksDO webHooksDO) {
        if(webHooksDO.getCommitAfter().equalsIgnoreCase(GIT_COMMIT_NULL)){
            // 空提交则跳过更新
            webHooksDO.setTriggerBuild(GitlabWebHooksDO.TriggerTypeEnum.skip.getCode());
            return ;
        }

        int ftEnvType = acqFtEnvType(webHooksDO);
        int trigger = GitlabWebHooksDO.TriggerTypeEnum.trigger.getCode();
        try {
            if (ftEnvType == GitlabWebHooksVO.EnvTypeEnum.tag.getCode()) {
                webHooksDO.setTriggerBuild(trigger);
                return;
            }
            if (ftEnvType == GitlabWebHooksVO.EnvTypeEnum.header.getCode()) {
                HashMap<String, String> configMap = acqConifMap();
                String ftBuildBranchs = configMap.get(JenkinsItemEnum.JENKINS_FT_BUILD_BRANCH.getItemKey());
                String[] bs = ftBuildBranchs.split(",");
                String branch = acqBranch(webHooksDO);
                for (String b : bs) {
                    if (branch.equalsIgnoreCase(b)) {
                        webHooksDO.setTriggerBuild(trigger);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 获取构建参数
     * mbranch   分支名称
     * deployPath   部署路径
     *
     * @param webHooks
     * @return
     */
    private HashMap<String, String> acqParams(GitlabWebHooksDO webHooks) {
        HashMap<String, String> params = new HashMap<>();
        params.put("mbranch", acqBranch(webHooks));
        params.put("deployPath", acqFtDeployPath(webHooks));
        // 项目名
        params.put("project", webHooks.getProjectName());
        // 仓库名
        params.put("repo", webHooks.getRepositoryName());
        return params;
    }

    /**
     * 通过ref判断环境类型
     * refs/heads/master
     * refs/tags/1.0.0
     *
     * @param webHooks
     * @return
     */
    private int acqFtEnvType(GitlabWebHooksDO webHooks) {
        String ref = webHooks.getRef();
        if (ref.indexOf(GIT_TAG_PREFIX) != -1)
            return GitlabWebHooksVO.EnvTypeEnum.tag.getCode();
        if (ref.indexOf(GIT_HEADER_PREFIX) != -1)
            return GitlabWebHooksVO.EnvTypeEnum.header.getCode();
        return GitlabWebHooksVO.EnvTypeEnum.err.getCode();
    }


    private String acqProjectName(GitlabWebHooksVO webHooks) {
        if (!StringUtils.isEmpty(webHooks.getProjectName())) return webHooks.getProjectName();
        if (StringUtils.isEmpty(webHooks.getRepositoryHomepage()))
            return "";
        try {
            URL url = new URL(webHooks.getRepositoryHomepage());
            String path = url.getPath();
            String s[] = path.split("/");
            String projectName = s[1];
            return projectName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * 获取部署路径
     * prod
     * http://cdn.52shangou.com/one/distribution/1.0.4/build/statement/index.js
     * one/distribution/1.0.4
     * <p>
     * daily
     * http://cdndaily.52shangou.com/one/distribution_branch/dev/build/statement/index.js
     * one/distribution_branch/dev
     *
     * @param webHooks
     * @return
     */
    private String acqFtDeployPath(GitlabWebHooksDO webHooks) {
        String projectName = webHooks.getProjectName();

        if (StringUtils.isEmpty(projectName)) return "";
        String repo = webHooks.getRepositoryName();

        if (StringUtils.isEmpty(repo)) return "";
        String branch = acqBranch(webHooks);

        if (StringUtils.isEmpty(branch)) return "";

        int buildType = acqFtEnvType(webHooks);
        if (buildType == GitlabWebHooksVO.EnvTypeEnum.err.getCode()) return "";

        HashMap<String, String> configMap = acqConifMap();
        String ossBucketFtOnline = configMap.get(JenkinsItemEnum.JENKINS_OSS_BUCKET_FT_ONLINE.getItemKey());
        if (buildType == GitlabWebHooksVO.EnvTypeEnum.tag.getCode()) {
            //System.err.println("deployPath=" + projectName + "/" + repo + "/" + branch);
            return ossBucketFtOnline + "/" + projectName + "/" + repo + "/" + branch;
        }

        if (buildType == GitlabWebHooksVO.EnvTypeEnum.header.getCode()) {
            //System.err.println("deployPath=" + projectName + "/" + repo + "_branch/" + branch);
            return projectName + "/" + repo + "_branch/" + branch;
        }
        return "";
    }

    private String acqBranch(GitlabWebHooksDO webHooks) {
        String ref = webHooks.getRef();
        int buildType = acqFtEnvType(webHooks);
        if (buildType == GitlabWebHooksVO.EnvTypeEnum.tag.getCode())
            return ref.replace(GIT_TAG_PREFIX, "");
        if (buildType == GitlabWebHooksVO.EnvTypeEnum.header.getCode())
            return ref.replace(GIT_HEADER_PREFIX, "");
        return "";
    }


}
