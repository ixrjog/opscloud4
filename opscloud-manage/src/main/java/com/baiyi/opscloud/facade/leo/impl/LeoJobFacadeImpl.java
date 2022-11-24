package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.facade.leo.tags.LeoTagHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.packer.leo.LeoJobPacker;
import com.baiyi.opscloud.service.leo.LeoBuildService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final LeoJobService leoJobService;

    private final LeoTemplateService leoTemplateService;

    private final LeoJobPacker leoJobPacker;

    private final LeoTagHelper leoTagHelper;

    private final LeoBuildService leoBuildService;

    @Override
    public DataTable<LeoJobVO.Job> queryLeoJobPage(LeoJobParam.JobPageQuery pageQuery) {
        DataTable<LeoJob> table = leoJobService.queryJobPage(pageQuery);
        List<LeoJobVO.Job> data = BeanCopierUtil.copyListProperties(table.getData(), LeoJobVO.Job.class).stream()
                .peek(e -> leoJobPacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void addLeoJob(LeoJobParam.AddJob addJob) {
        LeoJobModel.JobConfig jobConfig = LeoJobModel.load(addJob.getJobConfig());
        Optional.ofNullable(jobConfig)
                .map(LeoJobModel.JobConfig::getJob)
                .orElseThrow(() -> new LeoJobException("缺少任务配置！"));

        LeoTemplate leoTemplate = leoTemplateService.getById(addJob.getTemplateId());
        if (leoTemplate == null) {
            throw new LeoJobException("任务模板配置不正确！");
        }
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());
        String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse("0.0.0");
        LeoJob leoJob = BeanCopierUtil.copyProperties(addJob, LeoJob.class);
        leoJob.setTemplateVersion(templateVersion);
        leoJob.setTemplateContent(leoTemplate.getTemplateContent());
        leoJobService.add(leoJob);
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
        leoTagHelper.updateTagsWithLeoBusiness(leoJob, tags);
    }

    @Override
    @Transactional(rollbackFor = {LeoJobException.class})
    public void updateLeoJob(LeoJobParam.UpdateJob updateJob) {
        LeoTemplate leoTemplate = leoTemplateService.getById(updateJob.getTemplateId());
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
        LeoJob leoJob = leoJobService.getById(updateJob.getId());
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
        leoJobService.updateByPrimaryKeySelective(saveLeoJob);
        if (isUpdateTemplate) {
            // 变更模板
            this.upgradeLeoJobTemplateContent(updateJob.getId());
        }
        updateTagsWithLeoJob(saveLeoJob, jobConfig);
    }

    @Override
    public void upgradeLeoJobTemplateContent(int jobId) {
        LeoJob leoJob = leoJobService.getById(jobId);
        LeoTemplate leoTemplate = leoTemplateService.getById(leoJob.getTemplateId());
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
        leoJobService.updateByPrimaryKeySelective(saveLeoJob);
    }

    @Override
    public void deleteLeoJobById(int jobId) {
        LeoJob leoJob = leoJobService.getById(jobId);
        if (leoJob == null)
            throw new LeoJobException("删除任务错误，任务不存在！");
        if (leoBuildService.countWithJobId(jobId) > 0)
            throw new LeoJobException("删除任务错误，构建信息未删除！");
        leoJobService.deleteById(jobId);
    }

}
