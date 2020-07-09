package com.baiyi.opscloud.decorator.kubernetes;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.KubernetesUtils;
import com.baiyi.opscloud.common.util.TemplateUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplication;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesApplicationInstance;
import com.baiyi.opscloud.domain.vo.env.EnvVO;
import com.baiyi.opscloud.domain.vo.kubernetes.KubernetesTemplateVO;
import com.baiyi.opscloud.domain.vo.kubernetes.templateVariable.TemplateVariable;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.kubernetes.OcKubernetesApplicationService;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/6/30 2:07 下午
 * @Version 1.0
 */
@Component
public class KubernetesTemplateDecorator {

    @Resource
    private OcEnvService ocEnvService;

    @Resource
    private OcKubernetesApplicationService ocKubernetesApplicationService;

    public KubernetesTemplateVO.Template decorator(KubernetesTemplateVO.Template template, OcKubernetesApplicationInstance ocKubernetesApplicationInstance) {
        template = decorator(template);
        if (ocKubernetesApplicationInstance != null) {
            Map<String, String> variable = Maps.newHashMap();
            TemplateVariable templateVariable = KubernetesUtils.buildVariable(ocKubernetesApplicationInstance.getTemplateVariable());
            if (templateVariable != null)
                variable.putAll(templateVariable.getVariable());
            variable.put("envLabel", ocKubernetesApplicationInstance.getEnvLabel());
            OcKubernetesApplication ocKubernetesApplication = ocKubernetesApplicationService.queryOcKubernetesApplicationById(ocKubernetesApplicationInstance.getApplicationId());
            variable.put("appName", ocKubernetesApplication.getName());

            template.setTemplateYaml(TemplateUtils.getTemplate(template.getTemplateYaml(), variable));
        }
        return template;
    }

    public KubernetesTemplateVO.Template decorator(KubernetesTemplateVO.Template template) {
        // 装饰 环境信息
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(template.getEnvType());
        if (ocEnv != null) {
            EnvVO.Env env = BeanCopierUtils.copyProperties(ocEnv, EnvVO.Env.class);
            template.setEnv(env);
        }
        return template;
    }
}
