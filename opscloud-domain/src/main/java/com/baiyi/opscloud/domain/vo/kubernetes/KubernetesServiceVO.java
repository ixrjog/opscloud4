package com.baiyi.opscloud.domain.vo.kubernetes;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/1 1:51 下午
 * @Version 1.0
 */
public class KubernetesServiceVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Service extends BaseKubernetesApplicationVO.BaseProperty {

        private KubernetesClusterVO.Cluster cluster;
        private List<Port> ports;

        private Integer id;
        private Integer namespaceId;
        private String namespace;
        private String name;
        private String clusterIp;
        private String serviceType;
        private Date createTime;
        private Date updateTime;
        private String serviceYaml;
        private String serviceDetail;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Port {
        private Integer id;
        private Integer serviceId;
        private String name;
        private Integer nodePort;
        private Integer port;
        private Integer targetPort;
        private String protocol;
    }


}
