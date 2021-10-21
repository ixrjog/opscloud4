package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
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
public class KubernetesDsInstanceConfig extends BaseDsInstanceConfig {

    private Kubernetes kubernetes;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Kubernetes {

        private String version;
        private Kubeconfig kubeconfig;
        private Namespace namespace;
        private Deployment deployment;
        private Service service;

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
