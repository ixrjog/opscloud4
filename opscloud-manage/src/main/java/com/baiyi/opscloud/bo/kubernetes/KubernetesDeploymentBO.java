package com.baiyi.opscloud.bo.kubernetes;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/6/29 2:52 下午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesDeploymentBO {

    @Builder.Default
    private Integer applicationId =0;
    @Builder.Default
    private Integer instanceId = 0;
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
