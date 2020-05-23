package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/12 10:44 上午
 * @Version 1.0
 */
@Data
public class ResizeMessage extends BaseMessage {

    private String instanceId;
}
