package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/10/12 10:58 上午
 * @Version 1.0
 */
@Data
@Builder
public class DubboMappingServerBO {

    private Integer id;
    private Integer mappingId;
    private Integer serverId;
    private String serverName;
    private String ip;
    private Integer dubboPort;
    private Integer serverPort;
    @Builder.Default
    private Integer tcpMappingId = 0;
    private String comment;

}
