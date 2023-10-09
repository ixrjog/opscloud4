package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.core.factory.AssetProviderFactory;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.datasource.kubernetes.client.MyKubernetesClientBuilder;
import com.baiyi.opscloud.datasource.kubernetes.event.KubernetesPodWatch;
import com.baiyi.opscloud.datasource.kubernetes.event.KubernetesWatchEvent;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNamespaceDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesPodDriver;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesTestDriver;
import com.baiyi.opscloud.core.provider.base.asset.SimpleAssetProvider;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodList;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.dsl.LogWatch;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
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
    private DsConfigManager dsFactory;

    @Test
    void pullNamespaceTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeConstants.KUBERNETES_NAMESPACE.name());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void pullPodTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeConstants.KUBERNETES_POD.name());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void pullDeploymentTest() {
        SimpleAssetProvider assetProvider = AssetProviderFactory.getProvider(DsTypeEnum.KUBERNETES.getName(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name());
        assert assetProvider != null;
        assetProvider.pullAsset(5);
    }

    @Test
    void namespaceTest() {
        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        // KubernetesClient kubernetesClient = KubeClient.build(kubernetesDsInstanceConfig.getKubernetes());

        List<Namespace> namespaces = KubernetesNamespaceDriver.list(kubernetesDsInstanceConfig.getKubernetes());

        //  NamespaceList namespaceList = kubernetesClient.namespaces().list();
        for (Namespace item : namespaces) {
            System.err.print(item.getSpec());
        }
    }

    @Test
    void podTest() {
        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        KubernetesClient kubernetesClient = MyKubernetesClientBuilder.build(kubernetesDsInstanceConfig.getKubernetes());
        PodList podList = kubernetesClient.pods().inNamespace("dev").list();

        List<Pod> pods = podList.getItems().stream().filter(e -> e.getStatus().getPhase().equals("Running")).collect(Collectors.toList());

        for (Pod item : pods) {

            System.err.print(item.getSpec());
        }
    }

    @Test
    void podTest2() {
//        KubernetesDsInstanceConfig kubernetesDsInstanceConfig = (KubernetesDsInstanceConfig) getConfig();
//        KubernetesClient kubernetesClient = KubeClient.build(kubernetesDsInstanceConfig.getKubernetes());
//
//        Map<String, String> matchLabels =  kubernetesClient.apps()
//                .deployments()
//                .inNamespace("dev")
//                .withName("coms-dev-deployment")
//                .get()
//                .getSpec()
//                .getSelector()
//                .getMatchLabels();
//
//        List<Pod> items = client.pods().inNamespace(nsName).withLabels(matchLabels).list().getItems();
//        if (CollectionUtils.isEmpty(items)) {
//            return null;
//        }
        //  System.err.print(JSON.toJSONString(matchLabels));

        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        List<Pod> pods = KubernetesPodDriver.list(kubernetesDsInstanceConfig.getKubernetes(), "dev", "coms-dev-deployment");
        for (Pod item : pods) {
            System.err.print(item.getSpec());
        }
    }


    @Test
    void logTest() {
        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        LogWatch logWatch = KubernetesTestDriver.getPodLogWatch(kubernetesDsInstanceConfig.getKubernetes(), "dev", "coms-dev-deployment-5db56d8d8c-54svd");


        try {
            InputStream is = logWatch.getOutput();
            print(is);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        logWatch.close();

    }

    @Test
    void watchEventTest() {
        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        KubernetesWatchEvent.watch(kubernetesDsInstanceConfig.getKubernetes(),"dev");
    }

    @Test
    void watchEvent2Test() {
        KubernetesConfig kubernetesDsInstanceConfig = (KubernetesConfig) getConfig();
        KubernetesPodWatch.watch(kubernetesDsInstanceConfig.getKubernetes(),"dev");
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
    BaseDsConfig getConfig() {
        DatasourceConfig datasourceConfig = dsConfigService.getById(5);
        return dsFactory.build(datasourceConfig, KubernetesConfig.class);
    }
}
