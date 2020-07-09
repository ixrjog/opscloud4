package com.baiyi.opscloud.domain.vo.kubernetes;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/29 3:59 下午
 * @Version 1.0
 */
public class KubernetesDeploymentVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Deployment  extends BaseKubernetesApplicationVO.BaseProperty {

        private KubernetesClusterVO.Cluster cluster;

        private Object deployment;
        private String deploymentYAML;

        private Integer id;
        private Integer namespaceId;
        private String namespace;
        private String name;
        private String labelApp;
        private Integer availableReplicas;
        private Integer replicas;
        private Date createTime;
        private Date updateTime;
        private String deploymentDetail;
    }
}
