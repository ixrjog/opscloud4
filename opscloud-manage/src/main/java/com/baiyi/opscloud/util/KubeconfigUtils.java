package com.baiyi.opscloud.util;

import com.baiyi.opscloud.common.util.IOUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcKubernetesCluster;
import com.baiyi.opscloud.kubernetes.confg.KubernetesConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2020/6/28 2:36 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class KubeconfigUtils {

    private static KubernetesConfig kubernetesConfig;

    private KubeconfigUtils() {
    }

    @Autowired
    public void setKubernetesConfig(KubernetesConfig kubernetesConfig) {
        KubeconfigUtils.kubernetesConfig = kubernetesConfig;
    }

    public static void writeKubeconfig(OcKubernetesCluster ocKubernetesCluster) {
        if (StringUtils.isEmpty(ocKubernetesCluster.getKubeconfig())) return;
        String path = kubernetesConfig.acqKubeconfigPath(ocKubernetesCluster.getName());
        try {
            IOUtils.writeFile(ocKubernetesCluster.getKubeconfig(), path);
        } catch (Exception e) {
            log.error("写入kubeconfig错误，{}", path);
        }
    }

    public static void deleteKubeconfig(OcKubernetesCluster ocKubernetesCluster) {
        String path = kubernetesConfig.acqKubeconfigPath(ocKubernetesCluster.getName());
        try {
            IOUtils.delFile(path);
        } catch (Exception ignored) {
        }
    }
}
