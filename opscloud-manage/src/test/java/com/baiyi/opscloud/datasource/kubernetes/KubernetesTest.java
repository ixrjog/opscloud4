package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import com.baiyi.opscloud.common.type.DsAssetTypeEnum;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.AssetProviderFactory;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.datasource.kubernetes.client.KubeClient;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesNamespaceHandler;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesTestHandler;
import com.baiyi.opscloud.datasource.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.*;
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
            InputStream is = logWatch.getOutput();
            print(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logWatch.close();

    }


    public static void print(InputStream is) throws UnsupportedEncodingException {
        InputStreamReader isr = new InputStreamReader(is, "utf-8");
        BufferedReader br = new BufferedReader(isr);
        try {
            while ((br.read()) != -1) {
                System.out.println(br.readLine());
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
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
                System.out.println("111111111111111111");
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
