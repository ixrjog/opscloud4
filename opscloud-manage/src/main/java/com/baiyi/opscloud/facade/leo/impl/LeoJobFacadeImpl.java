package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.LeoJob;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.leo.LeoJobParam;
import com.baiyi.opscloud.domain.vo.leo.LeoJobVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.leo.domain.model.LeoJobModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoJobException;
import com.baiyi.opscloud.packer.leo.LeoJobPacker;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/4 14:45
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoJobFacadeImpl implements LeoJobFacade {

    private final LeoJobService leoJobService;

    private final LeoTemplateService leoTemplateService;

    private final LeoJobPacker leoJobPacker;

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
    }

    @Override
    public void updateLeoJob(LeoJobParam.UpdateJob updateJob) {
        LeoTemplate leoTemplate = leoTemplateService.getById(updateJob.getTemplateId());
        if (leoTemplate == null) {
            throw new LeoJobException("任务模板配置不正确！");
        }
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());
        String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElse("0.0.0");
        LeoJob leoJob = LeoJob.builder()
                .id(updateJob.getId())
                .name(updateJob.getName())
                .parentId(updateJob.getParentId())
                .applicationId(updateJob.getApplicationId())
                .branch(updateJob.getBranch())
                .templateId(updateJob.getTemplateId())
                .templateVersion(templateVersion)
                .templateContent(leoTemplate.getTemplateContent())
                .hide(updateJob.getHide())
                .href(updateJob.getHref())
                .isActive(updateJob.getIsActive())
                .comment(updateJob.getComment())
                .build();
        leoJobService.updateByPrimaryKeySelective(leoJob);
    }

}
