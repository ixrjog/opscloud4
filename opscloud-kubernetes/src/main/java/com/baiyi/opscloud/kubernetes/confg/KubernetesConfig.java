package com.baiyi.opscloud.kubernetes.confg;

import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/28 10:58 上午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "kubernetes", ignoreInvalidFields = true)
public class KubernetesConfig {

    private String version;
    private String dataPath;
    private KubernetesNamespaceConfig namespace;
    private KubernetesApplicationConfig application;

    private KubernetesDeploymentConfig deployment;
    private KubernetesDeploymentConfig service;

    private static final String DATA_PATH = "/data/opscloud-data/kubernetes";

    private static final String KUBERCONFIG_PATH = "kubeconfig";

    public String getApplicationInstanceNameByDeploymentName(String deploymentName) {
        return getInstanceNameByNomenclature(deployment.getNomenclature(), deploymentName);
    }

    public String getApplicationInstanceNameByServiceName(String serivceName) {
        return getInstanceNameByNomenclature(service.getNomenclature(), serivceName);
    }

    private String getInstanceNameByNomenclature(KubernetesNomenclatureConfig nomenclature, String name) {
        if (!StringUtils.isEmpty(nomenclature.getPrefix())) {
            String prefix = Joiner.on("").join("^", nomenclature.getPrefix(), "-");
            name = name.replaceAll(prefix, "");
        }
        if (!StringUtils.isEmpty(nomenclature.getSuffix())) {
            String suffix = Joiner.on("").join("-", nomenclature.getSuffix(), "$");
            name = name.replaceAll(suffix, "");
        }
        return name;
    }

    public String getDeploymentName(String applicationInstanceName) {
        String prefix = StringUtils.isEmpty(deployment.getNomenclature().getPrefix()) ? null : deployment.getNomenclature().getPrefix();
        String suffix = StringUtils.isEmpty(deployment.getNomenclature().getSuffix()) ? null : deployment.getNomenclature().getSuffix();
        return Joiner.on("-").skipNulls().join(prefix, applicationInstanceName, suffix);
    }

    public String getServiceName(String applicationInstanceName) {
        String prefix = StringUtils.isEmpty(service.getNomenclature().getPrefix()) ? null : service.getNomenclature().getPrefix();
        String suffix = StringUtils.isEmpty(service.getNomenclature().getSuffix()) ? null : service.getNomenclature().getSuffix();
        return Joiner.on("-").skipNulls().join(prefix, applicationInstanceName, suffix);
    }

    /**
     * 获取配置文件路径
     *
     * @return
     */
    public String acqKubeconfigPath(String clusterName) {
        if (StringUtils.isEmpty(dataPath)) {
            return Joiner.on("/").join(DATA_PATH, KUBERCONFIG_PATH, clusterName);
        } else {
            return Joiner.on("/").join(dataPath, KUBERCONFIG_PATH, clusterName);
        }
    }

    public List<String> getEnvLabelByEnvName(String envName) {
        if (application.getEnvLabel().containsKey(envName)) return application.getEnvLabel().get(envName);
        return Lists.newArrayList();
    }

    /**
     * 过滤命名空间
     *
     * @param namespace
     * @return
     */
    public boolean namespaceFilter(String namespace) {
        if (this.namespace.getFilter() == null) return false;
        return !this.namespace.getFilter().contains(namespace);
    }

}
