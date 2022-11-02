package com.baiyi.opscloud.facade.leo.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.LeoTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.Tag;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.leo.LeoTemplateParam;
import com.baiyi.opscloud.domain.param.tag.BusinessTagParam;
import com.baiyi.opscloud.domain.vo.leo.LeoTemplateVO;
import com.baiyi.opscloud.facade.leo.LeoTemplateFacade;
import com.baiyi.opscloud.facade.tag.SimpleTagFacade;
import com.baiyi.opscloud.leo.domain.model.LeoTemplateModel;
import com.baiyi.opscloud.leo.exception.LeoTemplateException;
import com.baiyi.opscloud.packer.leo.LeoTemplatePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoTemplateService;
import com.baiyi.opscloud.service.tag.TagService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.glassfish.jersey.internal.guava.Sets;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/11/1 16:37
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class LeoTemplateFacadeImpl implements LeoTemplateFacade {

    private final LeoTemplateService leoTemplateService;

    private final DsInstanceService dsInstanceService;

    private final TagService tagService;

    private final SimpleTagFacade simpleTagFacade;

    private final LeoTemplatePacker leoTemplatePacker;

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
    public LeoTemplateVO.Template addLeoTemplate(LeoTemplateParam.Template template) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(template.getTemplateConfig());
        // Jenkins 实例
        Optional<LeoTemplateModel.Instance> optionalInstance = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getJenkins)
                .map(LeoTemplateModel.Jenkins::getInstance);
        if (!optionalInstance.isPresent()) {
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项！");
        }
        LeoTemplateModel.Instance instance = optionalInstance.get();
        LeoTemplate leoTemplate = LeoTemplate.builder()
                .jenkinsInstanceUuid(getUuidWithJenkinsInstance(instance))
                .templateName(templateConfig.getTemplate().getName())
                .templateConfig(template.getTemplateConfig())
                .templateParameter(template.getTemplateParameter())
                .comment(template.getComment())
                .isActive(true)
                .build();
        leoTemplateService.add(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
        return toLeoTemplateVO(leoTemplate);
    }

    @Override
    public LeoTemplateVO.Template updateLeoTemplate(LeoTemplateParam.Template template) {
        LeoTemplateModel.TemplateConfig templateConfig = LeoTemplateModel.load(template.getTemplateConfig());
        // Jenkins 实例
        Optional<LeoTemplateModel.Instance> optionalInstance = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getJenkins)
                .map(LeoTemplateModel.Jenkins::getInstance);
        if (!optionalInstance.isPresent()) {
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项！");
        }
        LeoTemplateModel.Instance instance = optionalInstance.get();

        LeoTemplate leoTemplate = LeoTemplate.builder()
                .id(template.getId())
                .name(template.getName())
                .isActive(template.getIsActive())
                .jenkinsInstanceUuid(instance.getUuid())
                .templateConfig(template.getTemplateConfig())
                .build();

        leoTemplateService.updateByPrimaryKeySelective(leoTemplate);
        updateTagsWithLeoTemplate(leoTemplate, templateConfig);
        return toLeoTemplateVO(leoTemplate);
    }

    /**
     * 绑定标签
     *
     * @param leoTemplate
     * @param templateConfig
     */
    private void updateTagsWithLeoTemplate(LeoTemplate leoTemplate, LeoTemplateModel.TemplateConfig templateConfig) {
        Optional<List<String>> optionalTags = Optional.ofNullable(templateConfig)
                .map(LeoTemplateModel.TemplateConfig::getTemplate)
                .map(LeoTemplateModel.Template::getTags);
        if (!optionalTags.isPresent())
            return;
        Set<Integer> tagIds = Sets.newHashSet();
        optionalTags.get().stream().map(tagService::getByTagKey).filter(Objects::nonNull).map(Tag::getId).forEachOrdered(tagIds::add);
        BusinessTagParam.UpdateBusinessTags updateBusinessTags = BusinessTagParam.UpdateBusinessTags.builder()
                .businessId(leoTemplate.getId())
                .businessType(BusinessTypeEnum.LEO_TEMPLATE.getType())
                .tagIds(tagIds)
                .build();
        simpleTagFacade.updateBusinessTags(updateBusinessTags);
    }

    private String getUuidWithJenkinsInstance(LeoTemplateModel.Instance instance) {
        if (StringUtils.isNotBlank(instance.getUuid()))
            return instance.getUuid();
        if (!StringUtils.isNotBlank(instance.getName()))
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 未指定实例名称!");
        Optional<DatasourceInstance> optionalDsInstance = dsInstanceService.listByInstanceType(DsTypeEnum.JENKINS.getName()).stream()
                .filter(i -> i.getInstanceName().equals(instance.getName()))
                .findFirst();
        if (!optionalDsInstance.isPresent())
            throw new LeoTemplateException("模板配置缺少Jenkins实例配置项: 实例名称无效!");
        return optionalDsInstance.get().getUuid();
    }

}
