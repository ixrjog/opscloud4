package com.baiyi.opscloud.facade.template.impl;

import com.baiyi.opscloud.common.constants.TemplateKeyConstants;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.OCException;
import com.baiyi.opscloud.common.exception.template.BusinessTemplateException;
import com.baiyi.opscloud.common.template.YamlVars;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.MessageTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.common.OptionsVO;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.domain.vo.template.MessageTemplateVO;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;
import com.baiyi.opscloud.facade.template.TemplateFacade;
import com.baiyi.opscloud.facade.template.factory.ITemplateProvider;
import com.baiyi.opscloud.facade.template.factory.TemplateFactory;
import com.baiyi.opscloud.packer.template.BusinessTemplatePacker;
import com.baiyi.opscloud.packer.template.MessageTemplatePacker;
import com.baiyi.opscloud.packer.template.TemplatePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.MessageTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import com.baiyi.opscloud.util.OptionsUtil;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/12/6 10:58 AM
 * @Version 1.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TemplateFacadeImpl implements TemplateFacade {

    private final BusinessTemplateService businessTemplateService;

    private final MessageTemplateService messageTemplateService;

    private final TemplateService templateService;

    private final BusinessTemplatePacker businessTemplatePacker;

    private final MessageTemplatePacker messageTemplatePacker;

    private final TemplatePacker templatePacker;

    private final DsConfigManager dsConfigManager;

    private final EnvService envService;

    private final DsInstanceAssetService dsInstanceAssetService;

    private static final String TEMPLATE_KEY_SERVICE = "SERVICE";

    @Override
    public DataTable<TemplateVO.Template> queryTemplatePage(TemplateParam.TemplatePageQuery pageQuery) {
        DataTable<Template> table = templateService.queryPageByParam(pageQuery);
        List<TemplateVO.Template> data = BeanCopierUtil.copyListProperties(table.getData(), TemplateVO.Template.class)
                .stream()
                .peek(e -> templatePacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public TemplateVO.Template addTemplate(TemplateParam.Template template) {
        Template pre = BeanCopierUtil.copyProperties(template, Template.class);
        templateService.add(pre);
        return toVO(pre);
    }

    public TemplateVO.Template toVO(Template template) {
        TemplateVO.Template vo = BeanCopierUtil.copyProperties(template, TemplateVO.Template.class);
        templatePacker.wrap(vo, SimpleExtend.EXTEND);
        return vo;
    }

    @Override
    public TemplateVO.Template updateTemplate(TemplateParam.Template template) {
        Template preTemplate = BeanCopierUtil.copyProperties(template, Template.class);
        if (businessTemplateService.countByTemplateId(preTemplate.getId()) > 0) {
            preTemplate.setEnvType(null);
            preTemplate.setInstanceType(null);
            preTemplate.setTemplateKey(null);
            preTemplate.setTemplateType(null);
        }
        if (StringUtils.isBlank(template.getKind())) {
            preTemplate.setKind("default");
        }
        templateService.updateByPrimaryKeySelective(preTemplate);
        return toVO(templateService.getById(preTemplate.getId()));
    }

    @Override
    public void deleteTemplateById(int id) {
        List<BusinessTemplate> bizTemplates = businessTemplateService.queryByTemplateId(id);
        if (!CollectionUtils.isEmpty(bizTemplates)) {
            for (BusinessTemplate bizTemplate : bizTemplates) {
                businessTemplateService.deleteById(bizTemplate.getId());
            }
        }
        templateService.deleteById(id);
    }

    @Override
    public DataTable<BusinessTemplateVO.BusinessTemplate> queryBusinessTemplatePage(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery) {
        DataTable<BusinessTemplate> table = businessTemplateService.queryPageByParam(pageQuery);
        List<BusinessTemplateVO.BusinessTemplate> data = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(table.getData())) {
            data = BeanCopierUtil.copyListProperties(table.getData(), BusinessTemplateVO.BusinessTemplate.class)
                    .stream()
                    .peek(e -> businessTemplatePacker.wrap(e, pageQuery))
                    .collect(Collectors.toList());
        }
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public BusinessTemplateVO.BusinessTemplate createAssetByBusinessTemplate(int id) {
        BusinessTemplate bizTemplate = businessTemplateService.getById(id);
        if (bizTemplate == null) {
            throw new BusinessTemplateException("业务模板不存在!");
        }
        if (StringUtils.isBlank(bizTemplate.getName())) {
            throw new BusinessTemplateException("业务模板名称不合规（空值）!");
        }
        Template template = templateService.getById(bizTemplate.getTemplateId());
        if (template == null) {
            throw new BusinessTemplateException("模板不存在!");
        }
        ITemplateProvider templateProvider = TemplateFactory.getByInstanceAsset(template.getInstanceType(), template.getTemplateKey());
        if (templateProvider == null) {
            throw new BusinessTemplateException("无可用的业务模板供应商: instanceType={}, templateKey={}", template.getInstanceType(), template.getTemplateKey());
        }
        return templateProvider.produce(bizTemplate);
    }

    /**
     * 扫描业务模板与业务对象的关联关系
     *
     * @param instanceUuid
     */
    @Override
    public void scanBusinessTemplateByInstanceUuid(String instanceUuid) {
        List<BusinessTemplate> bizTemplates = businessTemplateService.queryByInstanceUuid(instanceUuid);
        if (CollectionUtils.isEmpty(bizTemplates)) {
            return;
        }
        bizTemplates.forEach(t -> {
            // 非资产
            if (BusinessTypeEnum.ASSET.getType() != t.getBusinessType()) {
                return;
            }
            if (!IdUtil.isEmpty(t.getBusinessId())) {
                if (dsInstanceAssetService.getById(t.getBusinessId()) != null) {
                    return;
                }
            }
            Template template = templateService.getById(t.getTemplateId());
            DatasourceInstanceAsset queryParam = DatasourceInstanceAsset.builder()
                    .instanceUuid(t.getInstanceUuid())
                    .assetType(Joiner.on("_").join(template.getInstanceType(), template.getTemplateKey()))
                    .name(t.getName())
                    .build();
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(queryParam);
            if (!CollectionUtils.isEmpty(assets) && assets.size() == 1) {
                t.setBusinessId(assets.getFirst().getId());
                businessTemplateService.update(t);
            }
        });
    }

    @Override
    public void deleteBusinessTemplateById(int id) {
        businessTemplateService.deleteById(id);
    }

    @Override
    public BusinessTemplateVO.BusinessTemplate addBusinessTemplate(BusinessTemplateParam.BusinessTemplate addBusinessTemplate) {
        Template template = templateService.getById(addBusinessTemplate.getTemplateId());
        if (template == null) {
            throw new OCException("关联模板不存在!");
        }
        BusinessTemplate bizTemplate = BeanCopierUtil.copyProperties(addBusinessTemplate, BusinessTemplate.class);

        bizTemplate.setEnvType(template.getEnvType());
        bizTemplate.setVars(template.getVars());
        bizTemplate.setVars(toVars(template.getVars(), addBusinessTemplate.getVars()));
        setName(bizTemplate);
        businessTemplateService.add(bizTemplate);

        BusinessTemplateVO.BusinessTemplate businessTemplateVO = BeanCopierUtil.copyProperties(bizTemplate, BusinessTemplateVO.BusinessTemplate.class);
        businessTemplatePacker.wrap(businessTemplateVO, SimpleExtend.EXTEND);
        return businessTemplateVO;
    }

    /**
     * 转换变量
     *
     * @param addVarsStr
     * @param templateVarsStr
     * @return
     */
    private String toVars(String templateVarsStr, String addVarsStr) {
        YamlVars.Vars vars = YamlUtil.loadVars(addVarsStr);
        YamlVars.Vars templateVars = YamlUtil.loadVars(templateVarsStr);

        templateVars.getVars().forEach((k, v) -> {
            if (vars.getVars().containsKey(k)) {
                templateVars.getVars().put(k, vars.getVars().get(k));
            }
        });
        return templateVars.dump();
    }

    @Override
    public BusinessTemplateVO.BusinessTemplate updateBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate) {
        BusinessTemplate preBizTemplate = businessTemplateService.getById(businessTemplate.getId());
        // 用户修改模板
        if (!preBizTemplate.getTemplateId().equals(businessTemplate.getTemplateId())) {
            Template template = templateService.getById(businessTemplate.getTemplateId());
            if (template == null) {
                throw new BusinessTemplateException("关联模板不存在!");
            }
            preBizTemplate.setTemplateId(businessTemplate.getTemplateId());
            preBizTemplate.setEnvType(template.getEnvType());
            preBizTemplate.setVars(template.getVars());
        } else {
            preBizTemplate.setVars(businessTemplate.getVars());
        }
        if (StringUtils.isEmpty(businessTemplate.getName())) {
            preBizTemplate.setName("");
            setName(preBizTemplate);
        }
        businessTemplateService.update(preBizTemplate);
        BusinessTemplateVO.BusinessTemplate businessTemplateVO = BeanCopierUtil.copyProperties(preBizTemplate, BusinessTemplateVO.BusinessTemplate.class);
        businessTemplatePacker.wrap(businessTemplateVO, SimpleExtend.EXTEND);
        return businessTemplateVO;
    }

    private void setName(BusinessTemplate bizTemplate) {
        try {
            if (StringUtils.isEmpty(bizTemplate.getName())) {
                Template template = templateService.getById(bizTemplate.getTemplateId());
                Env env = envService.getByEnvType(bizTemplate.getEnvType());
                YamlVars.Vars vars = YamlUtil.loadVars(bizTemplate.getVars());
                DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(bizTemplate.getInstanceUuid());
                KubernetesConfig.Kubernetes config = dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
                if (TemplateKeyConstants.DEPLOYMENT.name().equals(template.getTemplateKey())) {
                    setName(bizTemplate, config.getDeployment().getNomenclature(), vars, env);
                    return;
                }
                if (TemplateKeyConstants.SERVICE.name().equals(template.getTemplateKey())) {
                    setName(bizTemplate, config.getService().getNomenclature(), vars, env);
                }

                if (TemplateKeyConstants.INGRESS.name().equals(template.getTemplateKey())) {
                    setName(bizTemplate, config.getService().getNomenclature(), vars, env);
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    private void setName(BusinessTemplate bizTemplate, KubernetesConfig.Nomenclature nomenclature, YamlVars.Vars vars, Env env) {
        if (!vars.getVars().containsKey("appName")
                || StringUtils.isBlank(vars.getVars().get("appName"))) {
            return;
        }
        final String name = Joiner.on("").skipNulls().join(nomenclature.getPrefix(),
                vars.getVars().get("appName"),
                nomenclature.getSuffix(), "-", env.getEnvName()
        );
        bizTemplate.setName(name);
    }

    @Override
    public DataTable<MessageTemplateVO.MessageTemplate> queryMessageTemplatePage(MessageTemplateParam.MessageTemplatePageQuery pageQuery) {
        DataTable<MessageTemplate> table = messageTemplateService.queryPageByParam(pageQuery);
        List<MessageTemplateVO.MessageTemplate> data = BeanCopierUtil.copyListProperties(table.getData(), MessageTemplateVO.MessageTemplate.class)
                .stream()
                .peek(e -> messageTemplatePacker.wrap(e, pageQuery))
                .collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public void updateMessageTemplate(MessageTemplateParam.UpdateMessageTemplate messageTemplate) {
        MessageTemplate saveMessageTemplate = BeanCopierUtil.copyProperties(messageTemplate, MessageTemplate.class);
        messageTemplateService.updateByPrimaryKeySelective(saveMessageTemplate);
    }

    @Override
    public OptionsVO.Options getKindOptions() {
        List<String> kinds = templateService.getKindOptions();
        return OptionsUtil.toOptions(kinds);
    }

}
