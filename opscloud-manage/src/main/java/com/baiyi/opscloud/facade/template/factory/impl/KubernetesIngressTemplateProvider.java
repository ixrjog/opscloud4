package com.baiyi.opscloud.facade.template.factory.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesIngressDriver;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.BusinessTemplate;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.facade.template.factory.base.AbstractTemplateProvider;
import io.fabric8.kubernetes.api.model.networking.v1.Ingress;
import org.springframework.stereotype.Component;

import static com.baiyi.opscloud.common.constants.TemplateKeyConstants.INGRESS;

/**
 * @Author baiyi
 * @Date 2023/7/11 20:20
 * @Version 1.0
 */
@Component
public class KubernetesIngressTemplateProvider extends AbstractTemplateProvider<Ingress> {

    @Override
    protected Ingress produce(BusinessTemplate bizTemplate, String content) {
        DatasourceConfig dsConfig = dsConfigManager.getConfigByInstanceUuid(bizTemplate.getInstanceUuid());
        KubernetesConfig.Kubernetes config = dsConfigManager.build(dsConfig, KubernetesConfig.class).getKubernetes();
        if (KubernetesIngressDriver.get(config, content) == null) {
            return KubernetesIngressDriver.create(config, content);
        } else {
            return KubernetesIngressDriver.update(config, content);
        }
    }

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.KUBERNETES_INGRESS.name();
    }

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.getName();
    }


    @Override
    public String getTemplateKey() {
        return INGRESS.name();
    }

}