package com.baiyi.opscloud.server.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/4/2 10:49 上午
 * @Version 1.0
 */
@Data
@Builder
public class ZabbixInterfaceBO {

    @Builder.Default
    private Integer type = 1;
    @Builder.Default
    private Integer main = 1;
    @Builder.Default
    private Integer useip = 1;
    private String ip;
    @Builder.Default
    private String dns = "";
    @Builder.Default
    private String port = "10050";

}
