package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import com.baiyi.opscloud.factory.gitlab.enums.GitLabEventNameEnum;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

import static com.baiyi.opscloud.common.base.Global.AUTO_BUILD;
import static com.baiyi.opscloud.common.base.Global.AUTO_DEPLOY;

/**
 * @Author baiyi
 * @Date 2023/6/29 13:34
 * @Version 1.0
 */
@Slf4j
@Component
public class GitLabPushEventConsumer extends AbstractGitLabEventConsumer {

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private LeoBuildFacade leoBuildFacade;

    @Resource
    private SimpleTagService tagService;

    @Resource
    private EnvService envService;

    private static final String REFS_BRANCH_PREFIX = "refs/heads/";

    private final static GitLabEventNameEnum[] EVENT_NAME_ENUMS = {
            GitLabEventNameEnum.PUSH};

    private String getRef(GitLabNotifyParam.SystemHook systemHook) {
        if (StringUtils.isNotBlank(systemHook.getRef())) {
            return systemHook.getRef();
        }
        return CollectionUtils.isEmpty(systemHook.getRefs()) ? "" : systemHook.getRefs().getFirst();
    }

    protected void process(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        log.debug("gitLab systemHook with push event: {}", JSONUtil.writeValueAsString(systemHook));
        String ref = getRef(systemHook);
        if (StringUtils.isBlank(ref)) {
            return;
        }
        if (!ref.startsWith(REFS_BRANCH_PREFIX)) {
            return;
        }
        final String branch = ref.replace(REFS_BRANCH_PREFIX, "");
        final int gitLabUserId = Optional.of(systemHook)
                .map(GitLabNotifyParam.SystemHook::getUser_id)
                .orElse(0);

        if (gitLabUserId == 0) {
            return;
        }
        Optional<User> optionalUser = getUser(instance, gitLabUserId);
        if (optionalUser.isEmpty()) {
            // 用户不存在则不执行
            return;
        }

        final String sshUrl = Optional.of(systemHook)
                .map(GitLabNotifyParam.SystemHook::getProject)
                .map(GitLabNotifyParam.Project::getGit_ssh_url)
                .orElse("");

        if (StringUtils.isBlank(sshUrl)) {
            return;
        }

        List<ApplicationResource> resources = applicationResourceService.queryByResource(sshUrl, ApplicationResTypeEnum.GITLAB_PROJECT.name());
        if (!CollectionUtils.isEmpty(resources)) {
            resources.forEach(e -> process(e, branch, optionalUser.get().getUsername()));
        }

    }

    /**
     * process with applicationRes
     * @param resource
     * @param branch
     * @param username
     */
    private void process(ApplicationResource resource, String branch, String username) {
        final int applicationId = resource.getApplicationId();
        List<LeoJob> jobs = leoJobService.queryAutoBuildJob(applicationId, branch);
        // 查询标签
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }
        jobs.forEach(job -> process(job, branch, username));
    }

    /**
     * process with job
     * @param job
     * @param branch
     * @param username
     */
    private void process(LeoJob job, String branch, String username) {
        Env env = envService.getByEnvType(job.getEnvType());
        // AutoBuild
        if (tagService.hasBusinessTag(AUTO_BUILD, BusinessTypeEnum.LEO_JOB.getType(), job.getId(), false)) {
            // AutoDeploy
            if (tagService.hasBusinessTag(AUTO_DEPLOY, BusinessTypeEnum.LEO_JOB.getType(), job.getId(), false)) {
                Optional<ApplicationResource> optionalApplicationResource = applicationResourceService
                        .queryByApplication(job.getApplicationId(), ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                        .stream()
                        .filter(e -> e.getName().equals(Joiner.on(":").join(env.getEnvName(), job.getName())))
                        .findFirst();
                // 找到无状态
                if (optionalApplicationResource.isPresent()) {
                    handleBuild(job, branch, username, optionalApplicationResource.get().getBusinessId());
                    return;
                }
            }
            handleBuild(job, branch, username);
        }
    }

    /**
     * AutoBuild
     *
     * @param leoJob
     * @param branch
     */
    private void handleBuild(LeoJob leoJob, String branch, String username) {
        LeoBuildParam.DoAutoBuild doAutoBuild = LeoBuildParam.DoAutoBuild.builder()
                .username(username)
                .jobId(leoJob.getId())
                .autoBuild(true)
                .autoDeploy(false)
                .branch(branch)
                .build();
        leoBuildFacade.doAutoBuild(doAutoBuild);
    }

    /**
     * AutoBuild + AutoDeploy
     *
     * @param leoJob
     * @param branch
     * @param deploymentAssetId
     */
    private void handleBuild(LeoJob leoJob, String branch, String username, int deploymentAssetId) {
        LeoBuildParam.DoAutoBuild doAutoBuild = LeoBuildParam.DoAutoBuild.builder()
                .username(username)
                .jobId(leoJob.getId())
                .autoBuild(true)
                .autoDeploy(true)
                .assetId(deploymentAssetId)
                .branch(branch)
                .build();
        leoBuildFacade.doAutoBuild(doAutoBuild);
    }

    @Override
    protected GitLabEventNameEnum[] getEventNameEnums() {
        return EVENT_NAME_ENUMS;
    }

    @Override
    protected String getAssetType() {
        return DsAssetTypeConstants.GITLAB_PROJECT.name();
    }

}
