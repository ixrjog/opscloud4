package com.baiyi.opscloud.factory.gitlab.impl;

import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoBuildParam;
import com.baiyi.opscloud.domain.param.notify.gitlab.GitLabNotifyParam;
import com.baiyi.opscloud.facade.leo.LeoBuildFacade;
import com.baiyi.opscloud.factory.gitlab.GitLabEventNameEnum;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.tag.SimpleTagService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
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
@Component
public class GitLabPushEventConsumer extends AbstractGitLabEventConsumer {

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Resource
    private LeoJobService leoJobService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private UserService userService;

    @Resource
    private LeoBuildFacade leoBuildFacade;

    @Resource
    private SimpleTagService tagService;

    @Resource
    private EnvService envService;

    private static final String REFS_BRANCH_PREFIX = "refs/heads/";

    private final static GitLabEventNameEnum[] EVENT_NAME_ENUMS = {
            GitLabEventNameEnum.PUSH};

    protected void process(DatasourceInstance instance, GitLabNotifyParam.SystemHook systemHook) {
        if (!systemHook.getRef().startsWith(REFS_BRANCH_PREFIX)) {
            return;
        }
        final String branch = systemHook.getRef().replace(REFS_BRANCH_PREFIX, "");
        final int gitLabUserId = Optional.of(systemHook)
                .map(GitLabNotifyParam.SystemHook::getUser_id)
                .orElse(0);

        if (gitLabUserId == 0) {
            return;
        }
        User user = getUser(instance, gitLabUserId);
        if (user == null) {
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
            resources.forEach(e -> handle(e, branch));
        }

    }

    private User getUser(DatasourceInstance instance, int gitLabUserId) {
        DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                .instanceUuid(instance.getUuid())
                .assetType(DsAssetTypeConstants.GITLAB_USER.name())
                .assetId(String.valueOf(gitLabUserId))
                .build();

        List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(asset);
        if (assets.size() == 1) {
            String username = assets.get(0).getAssetKey();
            return userService.getByUsername(username);
        }
        return null;
    }

    private void handle(ApplicationResource resource, String branch) {
        final int applicationId = resource.getApplicationId();
        List<LeoJob> jobs = leoJobService.queryAutoBuildJob(applicationId, branch);
        // 查询标签
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }
        jobs.forEach(job -> {
            Env env = envService.getByEnvType(job.getEnvType());
            // AutoBuild
            if (tagService.hasBusinessTag(AUTO_BUILD, BusinessTypeEnum.LEO_JOB.getType(), job.getId())) {
                // AutoDeploy
                if (tagService.hasBusinessTag(AUTO_DEPLOY, BusinessTypeEnum.LEO_JOB.getType(), job.getId())) {
                    Optional<ApplicationResource> optionalApplicationResource = applicationResourceService.queryByApplication(job.getApplicationId(), ApplicationResTypeEnum.KUBERNETES_DEPLOYMENT.name())
                            .stream()
                            .filter(e -> e.getName().equals(Joiner.on(":").join(env.getEnvName(), job.getName())))
                            .findFirst();
                    // 找到无状态
                    if (optionalApplicationResource.isPresent()) {
                        handleBuild(job, branch,optionalApplicationResource.get().getBusinessId());
                        return;
                    }
                }
                handleBuild(job, branch);
            }
        });
    }

    /**
     * AutoBuild
     *
     * @param leoJob
     * @param branch
     */
    private void handleBuild(LeoJob leoJob, String branch) {
        LeoBuildParam.DoBuild doBuild = LeoBuildParam.DoBuild.builder()
                .jobId(leoJob.getId())
                .autoDeploy(false)
                .branch(branch)
                .build();
        leoBuildFacade.doBuild(doBuild);
    }

    /**
     * AutoBuild + AutoDeploy
     *
     * @param leoJob
     * @param branch
     * @param deploymentAssetId
     */
    private void handleBuild(LeoJob leoJob, String branch, int deploymentAssetId) {
        LeoBuildParam.DoBuild doBuild = LeoBuildParam.DoBuild.builder()
                .jobId(leoJob.getId())
                .autoDeploy(true)
                .assetId(deploymentAssetId)
                .branch(branch)
                .build();
        leoBuildFacade.doBuild(doBuild);
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
