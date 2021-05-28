package com.baiyi.caesar.terminal.message;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/11 10:47 上午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommandMessage extends BaseMessage {

    private String instanceId;
    private String data;

}
