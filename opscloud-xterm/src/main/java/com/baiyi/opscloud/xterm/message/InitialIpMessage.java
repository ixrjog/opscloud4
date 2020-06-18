package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/15 3:01 下午
 * @Version 1.0
 */
@Data
public class InitialIpMessage extends BaseMessage {

    // serverName
    private String instanceId;

    private String ip;

}