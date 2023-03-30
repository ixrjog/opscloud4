package com.baiyi.opscloud.datasource.kubernetes;

import com.baiyi.opscloud.datasource.kubernetes.base.BaseKubernetesTest;
import com.baiyi.opscloud.datasource.kubernetes.driver.KubernetesNodeDriver;
import io.fabric8.kubernetes.api.model.Node;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/24 3:08 下午
 * @Version 1.0
 */
public class KubernetesNodeTest extends BaseKubernetesTest {

    @Test
    void listNodeTest() {
        List<Node> nodes = KubernetesNodeDriver.list(getConfig().getKubernetes());
        print(nodes);
    }

}
