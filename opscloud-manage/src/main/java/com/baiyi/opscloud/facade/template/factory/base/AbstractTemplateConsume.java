package com.baiyi.opscloud.facade.template.factory.base;

import com.baiyi.opscloud.common.exception.common.CommonRuntimeException;
import com.baiyi.opscloud.common.template.YamlUtil;
import com.baiyi.opscloud.common.template.YamlVars;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.BeetlUtil;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.Env;
import com.baiyi.opscloud.domain.generator.opscloud.Template;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.vo.template.BusinessTemplateVO;
import com.baiyi.opscloud.datasource.facade.DsInstanceFacade;
import com.baiyi.opscloud.facade.template.factory.ITemplateConsume;
import com.baiyi.opscloud.facade.template.factory.TemplateFactory;
import com.baiyi.opscloud.packer.template.BusinessTemplatePacker;
import com.baiyi.opscloud.service.sys.EnvService;
import com.baiyi.opscloud.service.template.BusinessTemplateService;
import com.baiyi.opscloud.service.template.TemplateService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/12/7 4:22 PM
 * @Version 1.0
 */
public abstract class AbstractTemplateConsume<T> implements ITemplateConsume, InitializingBean {

    @Resource
    protected BusinessTemplateService businessTemplateService;

    @Resource
    private TemplateService templateService;

    @Resource
    private EnvService envService;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Resource
    protected DsInstanceFacade dsInstanceFacade;

    @Resource
    protected BusinessTemplatePacker businessTemplatePacker;

    private String renderTemplate(Template template, YamlVars.Vars vars) {
        try {
            return BeetlUtil.renderTemplate(template.getContent(), vars);
        } catch (IOException e) {
            e.printStackTrace();
            throw new CommonRuntimeException("无法创建资产: 渲染模板失败!");
        }
    }

    abstract protected T produce(BusinessTemplate bizTemplate, String content);

    public BusinessTemplateVO.BusinessTemplate produce(BusinessTemplate bizTemplate) {
        if (bizTemplate == null)
            throw new CommonRuntimeException("无法创建资产: 业务模板不存在!");
        Template template = templateService.getById(bizTemplate.getTemplateId());
        if (template == null)
            throw new CommonRuntimeException("无法创建资产: 模板不存在!");
        YamlVars.Vars vars = toVars(bizTemplate);
        // 渲染模板
        String content = renderTemplate(template, vars);
        T entity = produce(bizTemplate,content);
        List<DatasourceInstanceAsset> assets = dsInstanceFacade.pullAsset(bizTemplate.getInstanceUuid(), getAssetType(), entity);
        if (CollectionUtils.isEmpty(assets)) {
            throw new CommonRuntimeException("创建资产错误: 无法从生产者获取资产对象!");
        }
        bizTemplate.setBusinessId(assets.get(0).getId());
        businessTemplateService.update(bizTemplate); // 更新关联资产

        BusinessTemplateVO.BusinessTemplate businessTemplateVO = BeanCopierUtil.copyProperties(bizTemplate, BusinessTemplateVO.BusinessTemplate.class);
        businessTemplatePacker.wrap(businessTemplateVO, SimpleExtend.EXTEND);
        return businessTemplateVO;
    }

    private YamlVars.Vars toVars(BusinessTemplate bizTemplate) {
        YamlVars.Vars vars = YamlUtil.toVars(bizTemplate.getVars());
        if (!vars.getVars().containsKey("envName")) {
            Env env = envService.getByEnvType(bizTemplate.getEnvType());
            vars.getVars().put("envName", env.getEnvName());
        }
        return vars;
    }


    public void afterPropertiesSet() throws Exception {
        TemplateFactory.register(this);
    }
}
