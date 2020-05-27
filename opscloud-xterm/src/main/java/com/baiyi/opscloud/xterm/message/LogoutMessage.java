package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/11 5:18 下午
 * @Version 1.0
 */
@Data
public class LogoutMessage extends BaseMessage {

    private String instanceId;
}
