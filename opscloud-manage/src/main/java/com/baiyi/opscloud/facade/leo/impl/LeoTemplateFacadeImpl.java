package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.facade.leo.LeoJobFacade;
import com.baiyi.opscloud.facade.leo.LeoTemplateFacade;
import com.baiyi.opscloud.facade.leo.tags.LeoTagHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoTemplateException;
import com.baiyi.opscloud.leo.facade.JenkinsJobFacade;
import com.baiyi.opscloud.packer.leo.LeoTemplatePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoJobService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/1 16:37
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class LeoTemplateFacadeImpl implements LeoTemplateFacade {

    private final LeoTemplateService templateService;

    private final DsInstanceService dsInstanceService;

    private final LeoTemplatePacker templatePacker;

    private final DsConfigManager dsConfigManager;

    private final JenkinsJobFacade jenkinsJobFacade;

    private final LeoTagHelper tagHelper;

    private final LeoJobService jobService;

    private final LeoJobFacade jobFacade;

    @Override
    public DataTable<LeoTemplateVO.Template> queryLeoTemplatePage(LeoTemplateParam.TemplatePageQuery pageQuery) {
        DataTable<LeoTemplate> table = templateService.queryTemplatePage(pageQuery);
        List<LeoTemplateVO.Template> data = BeanCopierUtil.copyListProperties(table.getData(), LeoTemplateVO.Template.class).stream()
                .peek(e -> templatePacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    private LeoTemplateVO.Template toLeoTemplateVO(LeoTemplate leoTemplate) {
        LeoTemplateVO.Template templateVO = BeanCopierUtil.copyProperties(leoTemplate, LeoTemplateVO.Template.class);
        templatePacker.wrap(templateVO, SimpleExtend.EXTEND);
        return templateVO;
    }

    @Override
    public void addLeoTemplate(LeoTemplateParam.AddTemplate addTemplate) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(addTemplate.getTemplateConfig());
        // Jenkins 实例
        Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getJenkins)
                .map(LeoTemplateModel.Jenkins::getInstance)
                .orElseThrow(() -> new LeoTemplateException("Configuration does not exist: template->jenkins->instance"));

        LeoBaseModel.DsInstance instance = templateConfig.getTemplate().getJenkins().getInstance();
        LeoTemplate leoTemplate = LeoTemplate.builder()
                .name(addTemplate.getName())
                .jenkinsInstanceUuid(getUuidWithJenkinsInstance(instance))
                .templateName(templateConfig.getTemplate().getName())
                .templateConfig(addTemplate.getTemplateConfig())
                .templateParameter(addTemplate.getTemplateParameter())
                .comment(addTemplate.getComment())
                .isActive(true)
                .build();
        templateService.add(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
    }

    @Override
    public void updateLeoTemplate(LeoTemplateParam.UpdateTemplate updateTemplate) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(updateTemplate.getTemplateConfig());
        // Jenkins 实例
        Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getJenkins)
                .map(LeoTemplateModel.Jenkins::getInstance)
                .orElseThrow(() -> new LeoTemplateException("Configuration does not exist: template->jenkins->instance"));

        LeoBaseModel.DsInstance instance = templateConfig.getTemplate().getJenkins().getInstance();

        LeoTemplate leoTemplate = LeoTemplate.builder()
                .id(updateTemplate.getId())
                .name(updateTemplate.getName())
                .isActive(updateTemplate.getIsActive())
                .jenkinsInstanceUuid(getUuidWithJenkinsInstance(instance))
                .templateConfig(updateTemplate.getTemplateConfig())
                .build();

        templateService.updateByPrimaryKeySelective(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
        //return toLeoTemplateVO(leoTemplate);
    }

    @Override
    @Transactional(rollbackFor = {LeoTemplateException.class})
    public LeoTemplateVO.Template updateLeoTemplateContent(LeoTemplateParam.UpdateTemplate template) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(template.getJenkinsInstanceUuid());
        JenkinsConfig jenkinsConfig = dsConfigManager.build(dsConfig, JenkinsConfig.class);
        // 从DB中获取配置
        LeoTemplate leoTemplate = templateService.getById(template.getId());
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());

        Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .orElseThrow(() -> new LeoTemplateException("Configuration does not exist: template"));
        // https://leo-jenkins-1.chuanyinet.com/job/templates/job/tpl_test/
        String folder = Optional.of(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getFolder).orElse("");

        String jobXml = jenkinsJobFacade.getJobXml(templateConfig, jenkinsConfig, folder);
        leoTemplate.setTemplateContent(jobXml);
        templateService.updateByPrimaryKeySelective(leoTemplate);
        return toLeoTemplateVO(leoTemplate);
    }

    @Override
    public void uploadTemplate(int templateId) {
        LeoTemplate leoTemplate = templateService.getById(templateId);
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(leoTemplate.getJenkinsInstanceUuid());
        JenkinsConfig jenkinsConfig = dsConfigManager.build(dsConfig, JenkinsConfig.class);
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());

        String folder = Optional.of(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getFolder).orElse("");
        jenkinsJobFacade.createJob(templateConfig, jenkinsConfig, folder, leoTemplate.getTemplateContent());
    }

    /**
     * 绑定标签
     *
     * @param leoTemplate
     * @param templateConfig
     */
    private void updateTagsWithLeoTemplate(LeoTemplate leoTemplate, LeoTemplateModel.TemplateConfig templateConfig) {
        List<String> tags = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getTags)
                .orElse(Collections.emptyList());
        tagHelper.updateTagsWithLeoBusiness(leoTemplate, tags);
    }

    private String getUuidWithJenkinsInstance(LeoBaseModel.DsInstance instance) {
        if (StringUtils.isNotBlank(instance.getUuid())) {
            return instance.getUuid();
        }
        if (!StringUtils.isNotBlank(instance.getName())) {
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 未指定实例名称！");
        }
        Optional<DatasourceInstance> optionalDsInstance = dsInstanceService.listByInstanceType(DsTypeEnum.JENKINS.getName()).stream()
                .filter(i -> i.getInstanceName().equals(instance.getName()))
                .findFirst();
        if (optionalDsInstance.isEmpty()) {
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 实例名称无效！");
        }
        return optionalDsInstance.get().getUuid();
    }

    @Override
    public void deleteLeoTemplateById(int templateId) {
        LeoTemplate leoTemplate = templateService.getById(templateId);
        if (leoTemplate == null) {
            throw new LeoTemplateException("删除模板错误，模板不存在！");
        }
        if (jobService.countWithTemplateId(templateId) > 0) {
            throw new LeoTemplateException("删除模板错误，关联任务未删除！");
        }
        templateService.deleteById(templateId);
    }

    @Override
    public void upgradeLeoJobTemplate(int templateId) {
        LeoTemplate leoTemplate = templateService.getById(templateId);
        if (leoTemplate == null) {
            throw new LeoTemplateException("删除模板错误，模板不存在！");
        }
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());

        final String templateVersion = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getVersion)
                .orElseThrow(() -> new LeoTemplateException("Configuration does not exist: template->version"));
        List<LeoJob> jobs = jobService.queryUpgradeableJobs(templateId, templateVersion);
        if (CollectionUtils.isEmpty(jobs)) {
            return;
        }
        for (LeoJob job : jobs) {
            jobFacade.upgradeLeoJobTemplateContent(job, leoTemplate, templateVersion);
        }
    }

}