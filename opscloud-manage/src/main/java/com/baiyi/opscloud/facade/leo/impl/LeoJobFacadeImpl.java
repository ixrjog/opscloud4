package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.TemplateUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.param.leo.request.SubscribeLeoJobRequestParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.facade.leo.tags.LeoTagHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.base.BaseCrValidator;
import com.baiyi.opscloud.leo.handler.build.strategy.verification.validator.factory.CrValidatorFactory;
import com.baiyi.opscloud.leo.message.handler.impl.build.SubscribeLeoJobRequestHandler;
import com.baiyi.opscloud.packer.leo.LeoJobPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.*;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.baiyi.opscloud.leo.constants.BuildTypeConstants.KUBERNETES_IMAGE;


/**
 * @Author baiyi
 * @Date 2022/11/4 14:45
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoJobFacadeImpl implements LeoJobFacade {

    private final LeoJobService jobService;

    private final LeoTemplateService templateService;

    private final LeoJobPacker jobPacker;

    private final LeoTagHelper tagHelper;

    private final LeoBuildService buildService;

    private final LeoBuildLogService buildLogService;

    private final LeoDeployService deployService;

    private final LeoDeployLogService deployLogService;

    private final ApplicationService applicationService;

    private final ApplicationResourceService applicationResourceService;

    private final EnvService envService;

    private final SubscribeLeoJobRequestHandler subscribeLeoJobRequestHandler;

    private final static String INITIAL_VERSION = "0.0.1";

    @Override
    public DataTable<LeoJobVO.Job> queryLeoJobPage(LeoJobParam.JobPageQuery pageQuery) {
        DataTable<LeoJob> table = jobService.queryJobPage(pageQuery);
        List<LeoJobVO.Job> data = BeanCopierUtil.copyListProperties(table.getData(), LeoJobVO.Job.class).stream()
                .peek(e -> jobPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<LeoJobVO.Job> queryMyLeoJobPage(SubscribeLeoJobRequestParam pageQuery) {
        return subscribeLeoJobRequestHandler.queryLeoJobPage(pageQuery);
    }

    @Override
    public void addLeoJob(LeoJobParam.AddJob addJob) {
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(addJob.getJobConfig());

        Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .orElseThrow(() -> new LeoJobException("Configuration does not exist: job"));

        LeoTemplate leoTemplate = templateService.getById(addJob.getTemplateId());
        if (leoTemplate == null) {
            throw new LeoJobException("任务模板配置不正确！");
        }

        LeoJob leoJob = BeanCopierUtil.copyProperties(addJob, LeoJob.class);
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());
        final String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse(INITIAL_VERSION);

        fillLeoJob(leoJob, jobConfig);
        // jobKey转大写
        leoJob.setJobKey(leoJob.getJobKey().toUpperCase());
        leoJob.setTemplateContent(leoTemplate.getTemplateContent());
        leoJob.setTemplateVersion(templateVersion);
        jobService.add(leoJob);
        updateTagsWithLeoJob(leoJob, jobConfig);
    }

    private void fillLeoJob(LeoJob leoJob, LeoJobModel.JobConfig jobConfig) {
        final String branch = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getGitLab)
                .map(LeoBaseModel.GitLab::getProject)
                .map(LeoBaseModel.GitLabProject::getBranch)
                .orElse("");

        leoJob.setBranch(branch);
        final String buildType = Optional.of(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getBuild)
                .map(LeoJobModel.Build::getType)
                .orElse(KUBERNETES_IMAGE);
        leoJob.setBuildType(buildType);
    }

    /**
     * 绑定标签
     *
     * @param leoJob
     * @param jobConfig
     */
    private void updateTagsWithLeoJob(LeoJob leoJob, LeoJobModel.JobConfig jobConfig) {
        List<String> tags = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getTags)
                .orElse(Collections.emptyList());
        tagHelper.updateTagsWithLeoBusiness(leoJob, tags);
    }

    @Override
    @Transactional(rollbackFor = {LeoJobException.class})
    public void updateLeoJob(LeoJobParam.UpdateJob updateJob) {
        LeoTemplate leoTemplate = templateService.getById(updateJob.getTemplateId());
        if (leoTemplate == null) {
            throw new LeoJobException("任务模板配置不正确！");
        }
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate);
        final String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse(INITIAL_VERSION);
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(updateJob.getJobConfig());

        LeoJob leoJob = jobService.getById(updateJob.getId());
        boolean isUpdateTemplate = !leoJob.getTemplateId().equals(updateJob.getTemplateId());

        LeoJob saveLeoJob = LeoJob.builder()
                .id(updateJob.getId())
                .name(updateJob.getName())
                .parentId(updateJob.getParentId())
                .applicationId(updateJob.getApplicationId())
                .envType(updateJob.getEnvType())
                .jobConfig(updateJob.getJobConfig())
                .templateId(updateJob.getTemplateId())
                .templateVersion(isUpdateTemplate ? "-" : templateVersion)
                //.templateContent(leoTemplate.getTemplateContent())
                .hide(updateJob.getHide())
                .href(updateJob.getHref())
                .isActive(updateJob.getIsActive())
                .comment(updateJob.getComment())
                .build();
        fillLeoJob(saveLeoJob, jobConfig);

        jobService.updateByPrimaryKeySelective(saveLeoJob);
        if (isUpdateTemplate) {
            // 变更模板
            this.upgradeLeoJobTemplateContent(updateJob.getId());
        }
        updateTagsWithLeoJob(saveLeoJob, jobConfig);
    }

    @Override
    public void upgradeLeoJobTemplateContent(int jobId) {
        LeoJob leoJob = jobService.getById(jobId);
        LeoTemplate leoTemplate = templateService.getById(leoJob.getTemplateId());
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate);
        final String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElseThrow(() -> new LeoJobException("Configuration does not exist: template->version"));
        if (templateVersion.equals(leoJob.getTemplateVersion())) {
            throw new LeoJobException("任务模板版本已是最新版本无需升级！");
        }
        LeoJob saveLeoJob = LeoJob.builder()
                .id(leoJob.getId())
                .templateVersion(templateVersion)
                .templateContent(leoTemplate.getTemplateContent())
                .build();
        jobService.updateByPrimaryKeySelective(saveLeoJob);
        log.info("升级模板版本: jobId={}, templateVersion={}", leoJob.getId(), templateVersion);
    }

    @Override
    public void upgradeLeoJobTemplateContent(LeoJob leoJob, LeoTemplate leoTemplate, String templateVersion) {
        if (templateVersion.equals(leoJob.getTemplateVersion())) {
            return;
        }
        LeoJob saveLeoJob = LeoJob.builder()
                .id(leoJob.getId())
                .templateVersion(templateVersion)
                .templateContent(leoTemplate.getTemplateContent())
                .build();
        jobService.updateByPrimaryKeySelective(saveLeoJob);
        log.info("升级模板版本: jobId={}, templateVersion={}", leoJob.getId(), templateVersion);
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void createCrRepositoryWithLeoJobId(int jobId) {
        LeoJob leoJob = jobService.getById(jobId);
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(leoJob.getJobConfig());
        final LeoJobModel.CR cr = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getCr)
                .orElseThrow(() -> new LeoJobException("任务CR配置不存在无法验证镜像是否推送成功！"));

        final String crType = Optional.of(cr)
                .map(LeoJobModel.CR::getType)
                .orElseThrow(() -> new LeoJobException("任务CR类型配置不存在无法验证镜像是否推送成功！"));

        BaseCrValidator crValidator = CrValidatorFactory.getValidatorByCrType(crType);
        if (crValidator == null) {
            throw new LeoJobException("任务CR类型配置不正确: crType={}", crType);
        }
        try {
            crValidator.createRepository(leoJob, cr);
        } catch (LeoJobException e) {
            throw new LeoJobException("创建CR仓库错误: {}", e.getMessage());
        }
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteLeoJobById(int jobId) {
        LeoJob leoJob = jobService.getById(jobId);
        if (leoJob == null) {
            throw new LeoJobException("删除任务错误: 任务不存在！");
        }
        if (leoJob.getIsActive()) {
            throw new LeoJobException("删除任务错误: 当前任务有效！");
        }
        // 删除 deploy
        List<LeoDeploy> deploys = deployService.queryWithJobId(jobId);
        if (!CollectionUtils.isEmpty(deploys)) {
            for (LeoDeploy deploy : deploys) {
                log.info("删除部署日志: jobId={}, deployId={}", jobId, deploy.getId());
                deployLogService.deleteWithDeployId(deploy.getId());
                log.info("删除部署记录: deployId={}", deploy.getId());
                deployService.deleteById(deploy.getId());
            }
        }
        // 删除 build
        List<LeoBuild> builds = buildService.queryWithJobId(jobId);
        if (!CollectionUtils.isEmpty(builds)) {
            for (LeoBuild build : builds) {
                log.info("删除构建日志: jobId={}, buildId={}", jobId, build.getId());
                buildLogService.deleteWithBuildId(build.getId());
                log.info("删除构建记录: buildId={}", build.getId());
                buildService.deleteById(build.getId());
            }
        }
        log.info("删除LeoJob: jobId={}", jobId);
        jobService.deleteById(jobId);
    }

    @Override
    public void cloneJob(LeoJobParam.CloneJob cloneJob) {
        Application srcApplication = applicationService.getById(cloneJob.getSrcApplicationId());
        if (srcApplication == null) {
            throw new LeoJobException("复制任务错误: 源应用不存在！");
        }
        Application destApplication = applicationService.getById(cloneJob.getDestApplicationId());
        if (destApplication == null) {
            throw new LeoJobException("复制任务错误: 目标应用不存在！");
        }
        List<LeoJob> srcJobs = jobService.queryJobWithApplicationId(srcApplication.getId());
        if (CollectionUtils.isEmpty(srcJobs)) {
            throw new LeoJobException("复制任务错误: 源应用下任务为空！");
        }

        List<ApplicationResource> resources = applicationResourceService.queryByApplication(destApplication.getId(), ApplicationResTypeEnum.GITLAB_PROJECT.name(), BusinessTypeEnum.ASSET.getType());
        if (CollectionUtils.isEmpty(resources)) {
            throw new LeoJobException("复制任务错误: 应用未绑定GitLab项目！");
        }

        for (LeoJob srcJob : srcJobs) {
            // 校验任务
            if (CollectionUtils.isEmpty(jobService.queryJob(destApplication.getId(), srcJob.getEnvType()))) {
                Env env = envService.getByEnvType(srcJob.getEnvType());

                final String name = Joiner.on("-").join(destApplication.getName(), env.getEnvName());
                final String jobKey = Joiner.on("-").join(destApplication.getApplicationKey(), env.getEnvName().toUpperCase());

                String config = srcJob.getJobConfig().replaceAll(srcApplication.getName(), destApplication.getName());
                LeoJobModel.JobConfig jobConfig = LeoJobModel.load(config);

                // 修改GitLab project
                Optional<LeoBaseModel.GitLabProject> optionalProject = Optional.of(jobConfig)
                        .map(LeoJobModel.JobConfig::getJob)
                        .map(LeoJobModel.Job::getGitLab)
                        .map(LeoBaseModel.GitLab::getProject);
                optionalProject.ifPresent(gitLabProject -> gitLabProject.setSshUrl(resources.getFirst().getName()));

                LeoJob cloneLeoJob = LeoJob.builder()
                        .parentId(0)
                        .applicationId(destApplication.getId())
                        .name(name)
                        .jobKey(jobKey)
                        .branch(srcJob.getBranch())
                        .envType(srcJob.getEnvType())
                        .jobConfig(jobConfig.dump())
                        .buildNumber(0)
                        .templateId(srcJob.getTemplateId())
                        .templateVersion(srcJob.getTemplateVersion())
                        .templateContent(srcJob.getTemplateContent())
                        .buildType(srcJob.getBuildType())
                        .hide(false)
                        .isActive(true)
                        .build();
                jobService.add(cloneLeoJob);
                // 绑定Tags
                updateTagsWithLeoJob(cloneLeoJob, jobConfig);
                // 创建CR
                createCrRepositoryWithLeoJobId(cloneLeoJob.getId());
            }
        }
    }

    @Override
    public void cloneOneJob(LeoJobParam.CloneOneJob cloneOneJob) {
        LeoJob newLeoJob = jobService.getById(cloneOneJob.getJobId());
        newLeoJob.setId(null);
        Application application = applicationService.getById(newLeoJob.getApplicationId());
        Map<String, String> dict = Maps.newHashMap();
        dict.put("applicationName", application.getName());
        dict.put("env", envService.getByEnvType(newLeoJob.getEnvType()).getEnvName());
        final String jobName = TemplateUtil.render(cloneOneJob.getJobName(), dict);
        newLeoJob.setName(jobName);
        newLeoJob.setJobKey(jobName.toUpperCase());
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(newLeoJob);

        if (StringUtils.isNotBlank(cloneOneJob.getJobConfig())) {
            LeoJobModel.JobConfig jobConfigParam = LeoJobModel.load(cloneOneJob.getJobConfig());
            Optional<LeoJobModel.CR> optionalCR = Optional.of(jobConfigParam)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getCr);
            if (optionalCR.isPresent()) {
                LeoJobModel.CR cr = optionalCR.get();
                // type
                Optional<String> optionalType = Optional.of(cr)
                        .map(LeoJobModel.CR::getType);
                optionalType.ifPresent(type -> jobConfig.getJob().getCr().setType(type));
                // cloud
                Optional<LeoJobModel.Cloud> optionalCloud = Optional.of(cr)
                        .map(LeoJobModel.CR::getCloud);
                optionalCloud.ifPresent(cloud -> jobConfig.getJob().getCr().setCloud(cloud));
                // instance
                Optional<LeoJobModel.CRInstance> optionalCRInstance = Optional.of(cr)
                        .map(LeoJobModel.CR::getInstance);
                optionalCRInstance.ifPresent(crInstance -> jobConfig.getJob().getCr().setInstance(crInstance));
                // repo
                Optional<LeoJobModel.Repo> optionalRepo = Optional.of(cr)
                        .map(LeoJobModel.CR::getRepo);
                optionalRepo.ifPresent(crInstance -> jobConfig.getJob().getCr().setRepo(optionalRepo.get()));
            }
            // 合并参数
            Optional<List<LeoBaseModel.Parameter>> optionalParameters = Optional.of(jobConfigParam)
                    .map(LeoJobModel.JobConfig::getJob)
                    .map(LeoJobModel.Job::getParameters);
            if (optionalParameters.isPresent()) {
                List<LeoBaseModel.Parameter> parameters = optionalParameters.get();
                Map<String, LeoBaseModel.Parameter> pMap = jobConfig.getJob().getParameters()
                        .stream().collect(Collectors.toMap(LeoBaseModel.Parameter::getName, a -> a, (k1, k2) -> k1));
                parameters.forEach(p -> pMap.put(p.getName(), p));
                jobConfig.getJob().setParameters(pMap.keySet().stream().map(pMap::get).toList());

            }

            // tags
            if (cloneOneJob.getCloneTag()) {
                Optional<List<String>> optionalTags = Optional.of(jobConfigParam)
                        .map(LeoJobModel.JobConfig::getJob)
                        .map(LeoJobModel.Job::getTags);
                optionalTags.ifPresent(tags -> jobConfig.getJob().setTags(tags));
            }
        }

        newLeoJob.setJobConfig(jobConfig.dump());
        jobService.add(newLeoJob);
        // 更新任务Tags
        updateTagsWithLeoJob(newLeoJob, jobConfig);
    }

}