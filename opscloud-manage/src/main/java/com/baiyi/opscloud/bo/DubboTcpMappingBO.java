package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/10/14 1:58 下午
 * @Version 1.0
 */
@Data
@Builder
public class DubboTcpMappingBO {
    private Integer id;
    private Integer tcpPort;
    private String env;
    private String name;
}
