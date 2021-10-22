package com.baiyi.opscloud.datasource.kubernetes.provider;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.core.model.DsInstanceContext;
import com.baiyi.opscloud.core.provider.base.common.AbstractSetDsInstanceConfigProvider;
import com.baiyi.opscloud.core.util.SystemEnvUtil;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:14 下午
 * @Version 1.0
 */
@Component
public class KubernetesSetConfigProvider extends AbstractSetDsInstanceConfigProvider<KubernetesDsInstanceConfig.Kubernetes> {

    @Override
    public String getInstanceType() {
        return DsTypeEnum.KUBERNETES.name();
    }

    @Override
    protected KubernetesDsInstanceConfig.Kubernetes buildConfig(DatasourceConfig dsConfig) {
        return dsFactory.build(dsConfig, KubernetesDsInstanceConfig.class).getKubernetes();
    }

    @Override
    protected void doSet(DsInstanceContext dsInstanceContext) {
        KubernetesDsInstanceConfig.Kubernetes kubernetes = buildConfig(dsInstanceContext.getDsConfig());
        // 取配置文件路径
        String kubeconfigPath = SystemEnvUtil.renderEnvHome(kubernetes.getKubeconfig().getPath());
        Credential credential = getCredential(dsInstanceContext.getDsConfig().getCredentialId());
        String kubeconfig = stringEncryptor.decrypt(credential.getCredential());
        IOUtil.writeFile(kubeconfig, Joiner.on("/").join(kubeconfigPath, io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE));
    }
}
