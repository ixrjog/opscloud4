package com.baiyi.opscloud.facade.template.factory.base;

import com.baiyi.opscloud.common.builder.SimpleDictBuilder;
import com.baiyi.opscloud.common.exception.template.BusinessTemplateException;
import com.baiyi.opscloud.common.template.YamlVars;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.common.util.TemplateUtil;
import com.baiyi.opscloud.common.util.YamlUtil;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.facade.template.factory.ITemplateProvider;
import com.baiyi.opscloud.facade.template.factory.TemplateFactory;
import com.baiyi.opscloud.packer.template.BusinessTemplatePacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/12/7 4:22 PM
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractTemplateProvider<T> implements ITemplateProvider, InitializingBean {

    @Resource
    protected BusinessTemplateService businessTemplateService;

    @Resource
    private TemplateService templateService;

    @Resource
    private EnvService envService;

    @Resource
    protected DsConfigManager dsConfigManager;

    @Resource
    protected DsInstanceFacade<T> dsInstanceFacade;

    @Resource
    protected BusinessTemplatePacker businessTemplatePacker;

    @Resource
    private ApplicationService applicationService;

    @Resource
    protected ApplicationResourceService applicationResourceService;

    /**
     * 当值为true则会绑定应用资源
     *
     * @return
     */
    protected boolean hasApplicationResources() {
        return false;
    }

    private String renderTemplate(Template template, YamlVars.Vars vars) {
        try {
            return BeetlUtil.renderTemplate(template.getContent(), vars);
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new BusinessTemplateException("渲染模板失败!");
        }
    }

    /**
     * 生产
     *
     * @param bizTemplate
     * @param content
     * @return
     */
    abstract protected T produce(BusinessTemplate bizTemplate, String content);

    public BusinessTemplateVO.BusinessTemplate produce(BusinessTemplate bizTemplate) {
        if (bizTemplate == null) {
            throw new BusinessTemplateException("业务模板不存在!");
        }
        Template template = templateService.getById(bizTemplate.getTemplateId());
        if (template == null) {
            throw new BusinessTemplateException("模板不存在!");
        }
        YamlVars.Vars vars = toVars(bizTemplate);
        // 渲染模板
        String content = renderTemplate(template, vars);
        T entity = produce(bizTemplate, content);
        List<DatasourceInstanceAsset> assets = dsInstanceFacade.pullAsset(bizTemplate.getInstanceUuid(), getAssetType(), entity);
        if (CollectionUtils.isEmpty(assets)) {
            throw new BusinessTemplateException("无法从数据源获取资产对象!");
        }
        if (hasApplicationResources()) {
            addApplicationResource(assets, vars);
        }
        bizTemplate.setBusinessId(assets.getFirst().getId());
        // 更新关联资产
        businessTemplateService.update(bizTemplate);

        BusinessTemplateVO.BusinessTemplate businessTemplateVO = BeanCopierUtil.copyProperties(bizTemplate, BusinessTemplateVO.BusinessTemplate.class);
        businessTemplatePacker.wrap(businessTemplateVO, SimpleExtend.EXTEND);
        return businessTemplateVO;
    }

    private void addApplicationResource(List<DatasourceInstanceAsset> assets, YamlVars.Vars vars) {
        if (!vars.getVars().containsKey("appName")) {
            return;
        }
        final String appName = vars.getVars().get("appName");
        Application application = applicationService.getByName(appName);
        if (application == null) {
            return;
        }
        for (DatasourceInstanceAsset asset : assets) {
            if (applicationResourceService.getByUniqueKey(application.getId(), BusinessTypeEnum.ASSET.getType(), asset.getId()) == null) {
                ApplicationResource applicationResource = ApplicationResource.builder()
                        .applicationId(application.getId())
                        .businessType(BusinessTypeEnum.ASSET.getType())
                        .virtualResource(false)
                        .businessId(asset.getId())
                        .resourceType(asset.getAssetType())
                        .name(asset.getAssetId())
                        .build();
                applicationResourceService.add(applicationResource);
            }
        }
    }

    private YamlVars.Vars toVars(BusinessTemplate bizTemplate) {
        Env env = envService.getByEnvType(bizTemplate.getEnvType());
        // 模板内注入 envName
        Map<String, String> dict = SimpleDictBuilder.newBuilder()
                .put("envName", env.getEnvName())
                .build()
                .getDict();
        String varsStr = TemplateUtil.render(bizTemplate.getVars(), dict);
        YamlVars.Vars vars = YamlUtil.loadVars(varsStr);
        if (!vars.getVars().containsKey("envName")) {
            vars.getVars().put("envName", env.getEnvName());
        }
        return vars;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        TemplateFactory.register(this);
    }

}