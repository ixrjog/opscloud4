package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:47 上午
 * @Version 1.0
 */
@Data
public class CommandMessage extends BaseMessage {

    private String instanceId;
    private String data;

}
