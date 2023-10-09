package com.baiyi.opscloud.facade.template.factory.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.IstioDestinationRuleDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.facade.template.factory.base.AbstractTemplateProvider;
import io.fabric8.istio.api.networking.v1alpha3.DestinationRule;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.constants.TemplateKeyConstants.DESTINATION_RULE;

/**
 * @Author baiyi
 * @Date 2023/10/7 16:18
 * @Version 1.0
 */
@Component
public class IstioDestinationRuleTemplateProvider extends AbstractTemplateProvider<DestinationRule> {

    @Override
    protected DestinationRule produce(BusinessTemplate bizTemplate, String content) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(bizTemplate.getInstanceUuid());
        KubernetesConfig.Kubernetes config = dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
        return IstioDestinationRuleDriver.create(config, content);
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ISTIO_DESTINATION_RULE.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.getName();
    }

    @Override
    protected boolean hasApplicationResources() {
        return true;
    }

    @Override
    public String getTemplateKey() {
        return DESTINATION_RULE.name();
    }

}