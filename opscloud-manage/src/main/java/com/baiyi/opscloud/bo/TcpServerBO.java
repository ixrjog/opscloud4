package com.baiyi.opscloud.bo;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/10/19 11:47 上午
 * @Version 1.0
 */
@Data
@Builder
public class TcpServerBO {

    private String serverName;
    private String ip;
    private String port;
}
