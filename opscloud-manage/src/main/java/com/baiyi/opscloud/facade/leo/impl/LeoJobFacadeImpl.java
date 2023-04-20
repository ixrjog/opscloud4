package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.ApplicationResTypeEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.facade.leo.tags.LeoTagHelper;
import com.baiyi.opscloud.leo.handler.build.chain.post.validator.base.BaseCrValidator;
import com.baiyi.opscloud.leo.handler.build.chain.post.validator.factory.CrValidatorFactory;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.packer.leo.LeoJobPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.leo.*;
import com.baiyi.opscloud.service.sys.EnvService;
import com.google.common.base.Joiner;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


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

    @Override
    public DataTable<LeoJobVO.Job> queryLeoJobPage(LeoJobParam.JobPageQuery pageQuery) {
        DataTable<LeoJob> table = jobService.queryJobPage(pageQuery);
        List<LeoJobVO.Job> data = BeanCopierUtil.copyListProperties(table.getData(), LeoJobVO.Job.class).stream()
                .peek(e -> jobPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addLeoJob(LeoJobParam.AddJob addJob) {
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(addJob.getJobConfig());
        Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .orElseThrow(() -> new LeoJobException("缺少任务配置！"));

        LeoTemplate leoTemplate = templateService.getById(addJob.getTemplateId());
        if (leoTemplate == null) {
            throw new LeoJobException("任务模板配置不正确！");
        }
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());
        String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse("0.0.0");
        LeoJob leoJob = BeanCopierUtil.copyProperties(addJob, LeoJob.class);
        // JobKey转大写
        leoJob.setJobKey(leoJob.getJobKey().toUpperCase());
        leoJob.setTemplateVersion(templateVersion);
        leoJob.setTemplateContent(leoTemplate.getTemplateContent());
        jobService.add(leoJob);
        updateTagsWithLeoJob(leoJob, jobConfig);
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
                .orElse("0.0.0");
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(updateJob.getJobConfig());

        final String branch = Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .map(LeoJobModel.Job::getGitLab)
                .map(LeoBaseModel.GitLab::getProject)
                .map(LeoBaseModel.GitLabProject::getBranch)
                .orElse(updateJob.getBranch());
        LeoJob leoJob = jobService.getById(updateJob.getId());
        boolean isUpdateTemplate = !leoJob.getTemplateId().equals(updateJob.getTemplateId());

        LeoJob saveLeoJob = LeoJob.builder()
                .id(updateJob.getId())
                .name(updateJob.getName())
                .parentId(updateJob.getParentId())
                .applicationId(updateJob.getApplicationId())
                .envType(updateJob.getEnvType())
                .branch(branch)
                .jobConfig(updateJob.getJobConfig())
                .templateId(updateJob.getTemplateId())
                .templateVersion(isUpdateTemplate ? "-" : templateVersion)
                //.templateContent(leoTemplate.getTemplateContent())
                .hide(updateJob.getHide())
                .href(updateJob.getHref())
                .isActive(updateJob.getIsActive())
                .comment(updateJob.getComment())
                .build();
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
                .orElseThrow(() -> new LeoJobException("任务关联模板版本号配置不正确！"));
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
            if (CollectionUtils.isEmpty(jobService.queryJobWithApplicationIdAndEnvType(destApplication.getId(), srcJob.getEnvType()))) {
                Env env = envService.getByEnvType(srcJob.getEnvType());

                final String name = Joiner.on("-").join(destApplication.getName(), env.getEnvName());
                final String jobKey = Joiner.on("-").join(destApplication.getApplicationKey(), env.getEnvName().toUpperCase());

                String config = srcJob.getJobConfig().replaceAll(srcApplication.getName(), destApplication.getName());
                LeoJobModel.JobConfig jobConfig = LeoJobModel.load(config);

                // 修改GitLab project
                jobConfig.getJob().getGitLab().getProject()
                        .setSshUrl(resources.get(0).getName());

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

}
