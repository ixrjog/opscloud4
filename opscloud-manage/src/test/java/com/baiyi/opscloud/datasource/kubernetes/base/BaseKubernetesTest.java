package com.baiyi.opscloud.datasource.kubernetes.base;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.DefaultKubernetesClient;

import javax.annotation.Resource;

import static com.baiyi.opscloud.datasource.kubernetes.client.KubeClient.CONNECTION_TIMEOUT;
import static com.baiyi.opscloud.datasource.kubernetes.client.KubeClient.REQUEST_TIMEOUT;

/**
 * @Author baiyi
 * @Date 2021/11/24 3:07 下午
 * @Version 1.0
 */
public class BaseKubernetesTest extends BaseUnit {

    @Resource
    private DsConfigHelper dsConfigHelper;

    public interface KubernetesClusterConfigs {
        int EKS_TEST = 21;
        int EKS_GRAY = 26;
        int EKS_PROD = 30;

        int ACK_DEV = 6;
        int ACK_DAILY = 10;
        int ACK_GRAY = 13;
        int ACK_PROD = 14;

    }

    protected KubernetesConfig getConfigById(int id) {
        return dsConfigHelper.build(dsConfigHelper.getConfigById(id), KubernetesConfig.class);
    }

    protected KubernetesConfig getConfig() {
        //return dsConfigHelper.build(dsConfigHelper.getConfigByDsType(DsTypeEnum.KUBERNETES.getType()), KubernetesConfig.class);
        return dsConfigHelper.build(dsConfigHelper.getConfigById(30), KubernetesConfig.class);
    }

    public static DefaultKubernetesClient buildClient() {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                "/Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig");

        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                //.withMasterUrl kubeconfg中获取
                .withTrustCerts(true)
                .build();
        // /Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

    public static DefaultKubernetesClient buildClient(String token) {
        System.setProperty(io.fabric8.kubernetes.client.Config.KUBERNETES_KUBECONFIG_FILE,
                "/Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig");

        io.fabric8.kubernetes.client.Config config = new ConfigBuilder()
                //.withMasterUrl kubeconfg中获取
                .withOauthToken(token)
                .withTrustCerts(true)
                .build();
        // /Users/liangjian/opscloud-data/kubernetes/eks-test/kubeconfig
        config.setConnectionTimeout(CONNECTION_TIMEOUT);
        config.setRequestTimeout(REQUEST_TIMEOUT);
        return new DefaultKubernetesClient(config);
    }

}