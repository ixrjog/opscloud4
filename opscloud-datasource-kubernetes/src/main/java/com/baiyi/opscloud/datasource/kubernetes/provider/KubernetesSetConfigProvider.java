package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.constants.KubernetesProviders;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.datasource.kubernetes.exception.KubernetesException;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:14 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubernetesSetConfigProvider extends AbstractSetDsInstanceConfigProvider<KubernetesConfig.Kubernetes> {

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    protected KubernetesConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, KubernetesConfig.class).getKubernetes();
    }

    @Override
    protected void doSet(DsInstanceContext dsInstanceContext) {
        KubernetesConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        if (KubernetesProviders.AMAZON_EKS.getDesc().equals(kubernetes.getProvider())) {
            log.info("当前数据源不需要下发配置文件: provider={}", kubernetes.getProvider());
            return;
        }
        // 取配置文件路径
        String kubeconfigPath = SystemEnvUtil.renderEnvHome(kubernetes.getKubeconfig().getPath());
        if (StringUtils.isEmpty(kubeconfigPath)) {
            throw new KubernetesException("kubeconfig路径未配置!");
        }
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        String kubeconfig = stringEncryptor.decrypt(credential.getCredential());
        try {
            IOUtil.writeFile(kubeconfig, Joiner.on("/").join(kubeconfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE));
        } catch (Exception e) {
            throw new KubernetesException("写入配置文件错误: {}", e.getMessage());
        }
    }

}