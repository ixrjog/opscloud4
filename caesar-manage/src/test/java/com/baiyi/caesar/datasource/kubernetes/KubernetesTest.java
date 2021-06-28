package com.baiyi.caesar.datasource.kubernetes;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.caesar.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.caesar.common.type.DsAssetTypeEnum;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.factory.AssetProviderFactory;
import com.baiyi.caesar.datasource.factory.DsConfigFactory;
import com.baiyi.caesar.datasource.kubernetes.client.KubeClient;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesNamespaceHandler;
import com.baiyi.caesar.datasource.kubernetes.handler.KubernetesTestHandler;
import com.baiyi.caesar.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.caesar.domain.generator.caesar.DatasourceConfig;
import com.baiyi.caesar.service.datasource.DsConfigService;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/24 8:08 下午
 * @Version 1.0
 */
public class KubernetesTest extends BaseUnit {

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;


    @Test
    void pullNamespaceTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeEnum.KUBERNETES_NAMESPACE.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void pullPodTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeEnum.KUBERNETES_POD.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void pullDeploymentTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeEnum.KUBERNETES_DEPLOYMENT.getType());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void namespaceTest() {
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = (KubernetesDsInstanceConfig) getConfig();
        // KubernetesClient kubernetesClient = KubeClient.build(kubernetesDsInstanceConfig.getKubernetes());

        List<Namespace> namespaces = KubernetesNamespaceHandler.listNamespace(kubernetesDsInstanceConfig.getKubernetes());

        //  NamespaceList namespaceList = kubernetesClient.namespaces().list();
        for (Namespace item : namespaces) {
            System.err.print(item.getSpec());
        }
    }

    @Test
    void podTest() {
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = (KubernetesDsInstanceConfig) getConfig();
        KubernetesClient kubernetesClient = KubeClient.build(kubernetesDsInstanceConfig.getKubernetes());
        PodList podList = kubernetesClient.pods().inNamespace("dev").list();

        List<Pod> pods = podList.getItems().stream().filter(e -> e.getStatus().getPhase().equals("Running")).collect(Collectors.toList());

        for (Pod item : pods) {

            System.err.print(item.getSpec());
        }
    }


    @Test
    void logTest() {
        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = (KubernetesDsInstanceConfig) getConfig();
        LogWatch logWatch = KubernetesTestHandler.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(), "dev", "coms-dev-deployment-5db56d8d8c-54svd");


        try {
            readStream(logWatch.getOutput());
        } catch (Exception e) {

        }
    }


    /**
     * 读取流
     *
     * @param inStream
     * @return 字节数组
     * @throws Exception
     */
    public static void readStream(InputStream inStream) throws Exception {
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inStream), 1);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
            inStream.close();
            bufferedReader.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    @Test
    BaseDsInstanceConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(5);
        return dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
    }
}
