package com.baiyi.opscloud.bo.kubernetes;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/7/1 11:23 上午
 * @Version 1.0
 */
@Data
@Builder
public class KubernetesServicePortBO {

    private Integer id;
    private Integer serviceId;
    private String name;
    private Integer nodePort;
    private Integer port;
    private Integer targetPort;
    private String protocol;
    private Date createTime;
    private Date updateTime;
}
