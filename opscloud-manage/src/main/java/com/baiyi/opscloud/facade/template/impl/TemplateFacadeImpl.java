package com.baiyi.opscloud.facade.template.impl;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.template.YamlUtil;
import com.baiyi.opscloud.common.template.YamlVars;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.IdUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.template.BusinessTemplateParam;
import com.baiyi.opscloud.domain.param.template.TemplateParam;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.domain.vo.template.TemplateVO;
import com.baiyi.opscloud.facade.template.TemplateFacade;
import com.baiyi.opscloud.facade.template.factory.ITemplateConsume;
import com.baiyi.opscloud.facade.template.factory.TemplateFactory;
import com.baiyi.opscloud.packer.template.BusinessTemplatePacker;
import com.baiyi.opscloud.packer.template.TemplatePacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
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
@Service
@RequiredArgsConstructor
public class TemplateFacadeImpl implements TemplateFacade {

    private final BusinessTemplateService businessTemplateService;

    private final TemplateService templateService;

    private final BusinessTemplatePacker businessTemplatePacker;

    private final TemplatePacker templatePacker;

    private final DsInstanceService dsInstanceService;

    private final DsInstancePacker dsInstancePacker;

    private final DsConfigHelper dsConfigHelper;

    private final EnvService envService;

    private final DsInstanceFacade dsInstanceFacade;

    private final DsInstanceAssetService dsInstanceAssetService;

    @Override
    public DataTable<TemplateVO.Template> queryTemplatePage(TemplateParam.TemplatePageQuery pageQuery) {
        DataTable<Template> table = templateService.queryPageByParam(pageQuery);

        List<TemplateVO.Template> data = BeanCopierUtil.copyListProperties(table.getData(), TemplateVO.Template.class).stream().peek(e ->
                templatePacker.wrap(e, pageQuery)
        ).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public TemplateVO.Template addTemplate(TemplateParam.Template template) {
        Template pre = BeanCopierUtil.copyProperties(template, Template.class);
        templateService.add(pre);
        return toVO(pre);
    }

    public TemplateVO.Template toVO(Template template){
        TemplateVO.Template vo = BeanCopierUtil.copyProperties(template, TemplateVO.Template.class);
        templatePacker.wrap(vo , SimpleExtend.EXTEND);
        return vo ;
    }

    @Override
    public TemplateVO.Template updateTemplate(TemplateParam.Template template) {
        Template pre = BeanCopierUtil.copyProperties(template, Template.class);
        if (businessTemplateService.countByTemplateId(pre.getId()) > 0) {
            pre.setEnvType(null);
            pre.setInstanceType(null);
            pre.setTemplateKey(null);
            pre.setTemplateType(null);
        }
        templateService.updateSelective(pre);
        return toVO(templateService.getById(pre.getId()));
    }

    @Override
    public void deleteTemplateById(int id) {
        if (businessTemplateService.countByTemplateId(id) > 0)
            throw new CommonRuntimeException("模板已经使用无法删除！");
        templateService.deleteById(id);
    }

    @Override
    public DataTable<BusinessTemplateVO.BusinessTemplate> queryBusinessTemplatePage(BusinessTemplateParam.BusinessTemplatePageQuery pageQuery) {
        DataTable<BusinessTemplate> table = businessTemplateService.queryPageByParam(pageQuery);

        List<BusinessTemplateVO.BusinessTemplate> data = Lists.newArrayList();
        if (!CollectionUtils.isEmpty(table.getData())) {
            data = BeanCopierUtil.copyListProperties(table.getData(), BusinessTemplateVO.BusinessTemplate.class).stream().peek(e -> businessTemplatePacker.wrap(e, pageQuery)).collect(Collectors.toList());
        }
        return new DataTable<>(data, table.getTotalNum());
    }

    private YamlVars.Vars toVars(BusinessTemplate bizTemplate) {
        YamlVars.Vars vars = YamlUtil.toVars(bizTemplate.getVars());
        if (!vars.getVars().containsKey("envName")) {
            Env env = envService.getByEnvType(bizTemplate.getEnvType());
            vars.getVars().put("envName", env.getEnvName());
        }
        return vars;
    }

    @Override
    public BusinessTemplateVO.BusinessTemplate createAssetByBusinessTemplate(int id) {
        BusinessTemplate bizTemplate = businessTemplateService.getById(id);
        if (bizTemplate == null)
            throw new CommonRuntimeException("无法创建资产: 业务模板不存在!");
        if (StringUtils.isEmpty(bizTemplate.getName()))
            throw new CommonRuntimeException("无法创建资产: 业务模板名称不合规（空值）!");
        Template template = templateService.getById(bizTemplate.getTemplateId());
        if (template == null)
            throw new CommonRuntimeException("无法创建资产: 模板不存在!");
        ITemplateConsume iTemplateConsume = TemplateFactory.getByInstanceAsset(template.getInstanceType(), template.getTemplateKey());
        if (iTemplateConsume == null) {
            throw new CommonRuntimeException("无法创建资产: 无可用的生产者!");
        }
        return iTemplateConsume.produce(bizTemplate);
    }

    /**
     * 扫描业务模板与业务对象的关联关系
     *
     * @param instanceUuid
     */
    @Override
    public void scanBusinessTemplateByInstanceUuid(String instanceUuid) {
        List<BusinessTemplate> bizTemplates = businessTemplateService.queryByInstanceUuid(instanceUuid);
        if (CollectionUtils.isEmpty(bizTemplates))
            return;
        bizTemplates.forEach(t -> {
            // 非资产
            if (BusinessTypeEnum.ASSET.getType() != t.getBusinessType())
                return;
            if (!IdUtil.isEmpty(t.getBusinessId())) {
                if (dsInstanceAssetService.getById(t.getBusinessId()) != null)
                    return;
            }
            Template template = templateService.getById(t.getTemplateId());
            DatasourceInstanceAsset queryParam = DatasourceInstanceAsset.builder()
                    .instanceUuid(t.getInstanceUuid())
                    .assetType(Joiner.on("_").join(template.getInstanceType(), template.getTemplateKey()))
                    .name(t.getName())
                    .build();
            List<DatasourceInstanceAsset> assets = dsInstanceAssetService.queryAssetByAssetParam(queryParam);
            if (!CollectionUtils.isEmpty(assets) && assets.size() == 1) {
                t.setBusinessId(assets.get(0).getId());
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
        BusinessTemplate bizTemplate = BeanCopierUtil.copyProperties(addBusinessTemplate, BusinessTemplate.class);
        Template template = templateService.getById(bizTemplate.getTemplateId());
        if (template == null)
            throw new CommonRuntimeException("无法创建业务模板: 模板不存在!");
        bizTemplate.setEnvType(template.getEnvType());
        bizTemplate.setVars(template.getVars());
        setName(bizTemplate);
        businessTemplateService.add(bizTemplate);

        BusinessTemplateVO.BusinessTemplate businessTemplateVO = BeanCopierUtil.copyProperties(bizTemplate, BusinessTemplateVO.BusinessTemplate.class);
        businessTemplatePacker.wrap(businessTemplateVO, SimpleExtend.EXTEND);
        return businessTemplateVO;
    }

    @Override
    public BusinessTemplateVO.BusinessTemplate updateBusinessTemplate(BusinessTemplateParam.BusinessTemplate businessTemplate) {
        BusinessTemplate preBizTemplate = businessTemplateService.getById(businessTemplate.getId());
        // 用户修改模版
        if (!preBizTemplate.getTemplateId().equals(businessTemplate.getTemplateId())) {
            Template template = templateService.getById(businessTemplate.getTemplateId());
            if (template == null)
                throw new CommonRuntimeException("无法创建业务模板: 模板不存在!");
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
                YamlVars.Vars vars = YamlUtil.toVars(bizTemplate.getVars());
                DatasourceConfig dsConfig = dsConfigHelper.getConfigByInstanceUuid(bizTemplate.getInstanceUuid());
                KubernetesConfig.Kubernetes config = dsConfigHelper.build(dsConfig, KubernetesConfig.class).getKubernetes();
                if ("DEPLOYMENT".equals(template.getTemplateKey())) {
                    setName(bizTemplate, config.getDeployment().getNomenclature(), vars, env);
                    return;
                }
                if ("SERVICE".equals(template.getTemplateKey())) {
                    setName(bizTemplate, config.getService().getNomenclature(), vars, env);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setName(BusinessTemplate bizTemplate, KubernetesConfig.Nomenclature nomenclature, YamlVars.Vars vars, Env env) {
        if (!vars.getVars().containsKey("appName")
                || StringUtils.isBlank(vars.getVars().get("appName"))) return;
        String name = Joiner.on("").skipNulls().join(nomenclature.getPrefix(),
                vars.getVars().get("appName"),
                nomenclature.getSuffix(), "-", env.getEnvName()
        );
        bizTemplate.setName(name);
    }
}
