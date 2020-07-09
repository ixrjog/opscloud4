package com.baiyi.opscloud.bo.kubernetes;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/1 10:07 上午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesServiceBO {

    @Builder.Default
    private Integer applicationId = 0;
    @Builder.Default
    private Integer instanceId = 0;
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
