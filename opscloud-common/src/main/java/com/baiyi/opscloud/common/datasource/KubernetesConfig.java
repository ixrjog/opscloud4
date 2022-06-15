package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/24 4:46 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class KubernetesConfig extends BaseConfig {

    private Kubernetes kubernetes;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Kubernetes {

        private String version;
        private String provider;
        private AmazonEks amazonEks;
        private Kubeconfig kubeconfig;
        private Namespace namespace;
        private Container container;
        private Deployment deployment;
        private Service service;

    }

    /**
     * amazonEks:
     * region: us-east-1
     * clusterName: eksworkshop-eksctl
     * url: https://D1E85F6BCC9BC11111D2C1126ADA7970.yl4.eu-west-1.eks.amazonaws.com
     * accessKeyId: ${credentialAccessKey}
     * secretKey: ${credentialSecret}
     */
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AmazonEks {

        private String region;
        private String clusterName;
        private String url;
        private String accessKeyId;
        private String secretKey;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Kubeconfig {
        private String path;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Namespace {
        private List<String> ignore;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Container {
        private List<String> ignore;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Deployment {
        private Nomenclature nomenclature;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Service extends Deployment {
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Nomenclature {
        private String prefix;
        private String suffix;
    }
}
