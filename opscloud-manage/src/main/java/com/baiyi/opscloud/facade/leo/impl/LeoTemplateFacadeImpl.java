package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.JenkinsConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.facade.leo.LeoTemplateFacade;
import com.baiyi.opscloud.facade.leo.tags.LeoTagHelper;
import com.baiyi.opscloud.leo.domain.model.LeoBaseModel;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoTemplateException;
import com.baiyi.opscloud.leo.helper.JenkinsJobHelper;
import com.baiyi.opscloud.packer.leo.LeoTemplatePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final LeoTemplateService leoTemplateService;

    private final DsInstanceService dsInstanceService;

    private final LeoTemplatePacker leoTemplatePacker;

    private final DsConfigHelper dsConfigHelper;

    private final JenkinsJobHelper jenkinsJobHelper;

    private final LeoTagHelper leoTagHelper;

    @Override
    public DataTable<LeoTemplateVO.Template> queryLeoTemplatePage(LeoTemplateParam.TemplatePageQuery pageQuery) {
        DataTable<LeoTemplate> table = leoTemplateService.queryTemplatePage(pageQuery);
        List<LeoTemplateVO.Template> data = BeanCopierUtil.copyListProperties(table.getData(), LeoTemplateVO.Template.class).stream()
                .peek(e -> leoTemplatePacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    private LeoTemplateVO.Template toLeoTemplateVO(LeoTemplate leoTemplate) {
        LeoTemplateVO.Template templateVO = BeanCopierUtil.copyProperties(leoTemplate, LeoTemplateVO.Template.class);
        leoTemplatePacker.wrap(templateVO, SimpleExtend.EXTEND);
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
                .orElseThrow(() -> new LeoTemplateException("模板配置缺少Jenkins实例配置项！"));

        LeoBaseModel.DsInstance instance = templateConfig.getTemplate().getJenkins().getInstance();
        LeoTemplate leoTemplate = LeoTemplate.builder()
                .jenkinsInstanceUuid(getUuidWithJenkinsInstance(instance))
                .templateName(templateConfig.getTemplate().getName())
                .templateConfig(addTemplate.getTemplateConfig())
                .templateParameter(addTemplate.getTemplateParameter())
                .comment(addTemplate.getComment())
                .isActive(true)
                .build();
        leoTemplateService.add(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
        //return toLeoTemplateVO(leoTemplate);
    }

    @Override
    public void updateLeoTemplate(LeoTemplateParam.UpdateTemplate updateTemplate) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(updateTemplate.getTemplateConfig());
        // Jenkins 实例
        Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getJenkins)
                .map(LeoTemplateModel.Jenkins::getInstance)
                .orElseThrow(() -> new LeoTemplateException("模板配置缺少Jenkins实例配置项！"));

        LeoBaseModel.DsInstance instance = templateConfig.getTemplate().getJenkins().getInstance();

        LeoTemplate leoTemplate = LeoTemplate.builder()
                .id(updateTemplate.getId())
                .name(updateTemplate.getName())
                .isActive(updateTemplate.getIsActive())
                .jenkinsInstanceUuid(getUuidWithJenkinsInstance(instance))
                .templateConfig(updateTemplate.getTemplateConfig())
                .build();

        leoTemplateService.updateByPrimaryKeySelective(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
        //return toLeoTemplateVO(leoTemplate);
    }

    @Override
    @Transactional(rollbackFor = {LeoTemplateException.class})
    public LeoTemplateVO.Template updateLeoTemplateContent(LeoTemplateParam.UpdateTemplate template) {
        DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(template.getJenkinsInstanceUuid());
        JenkinsConfig jenkinsConfig = dsConfigHelper.build(dsConfig, JenkinsConfig.class);
        // 从DB中获取配置
        LeoTemplate leoTemplate = leoTemplateService.getById(template.getId());
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(leoTemplate.getTemplateConfig());

        Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .orElseThrow(() -> new LeoTemplateException("任务模板未配置！"));
        // https://leo-jenkins-1.chuanyinet.com/job/templates/job/tpl_test/
        String folder = Optional.of(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getFolder).orElse("");

        String jobXml = jenkinsJobHelper.getJobXml(templateConfig, jenkinsConfig, folder);
        leoTemplate.setTemplateContent(jobXml);
        leoTemplateService.updateByPrimaryKeySelective(leoTemplate);
        return toLeoTemplateVO(leoTemplate);
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
        leoTagHelper.updateTagsWithLeoBusiness(leoTemplate, tags);
    }

    private String getUuidWithJenkinsInstance(LeoBaseModel.DsInstance instance) {
        if (StringUtils.isNotBlank(instance.getUuid()))
            return instance.getUuid();
        if (!StringUtils.isNotBlank(instance.getName()))
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 未指定实例名称！");
        Optional<DatasourceInstance> optionalDsInstance = dsInstanceService.listByInstanceType(DsTypeEnum.JENKINS.getName()).stream()
                .filter(i -> i.getInstanceName().equals(instance.getName()))
                .findFirst();
        if (!optionalDsInstance.isPresent())
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 实例名称无效！");
        return optionalDsInstance.get().getUuid();
    }

}
