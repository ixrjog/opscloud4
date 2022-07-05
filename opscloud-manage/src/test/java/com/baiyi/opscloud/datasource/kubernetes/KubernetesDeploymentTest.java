package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesDeploymentDriver;
import io.fabric8.kubernetes.api.model.apps.Deployment;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2022/7/4 16:51
 * @Version 1.0
 */
public class KubernetesDeploymentTest extends BaseKubernetesTest {


    @Test
    void aTest() {
        Deployment deployment = KubernetesDeploymentDriver.getDeployment(getConfig().getKubernetes(), "dev", "merchant-rss-dev");
        KubernetesDeploymentDriver.redeployDeployment(getConfig().getKubernetes(), "dev", "merchant-rss-dev");

        print(deployment);
    }


}
