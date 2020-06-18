package com.baiyi.opscloud.xterm.message;

import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:27 上午
 * @Version 1.0
 */
@Data
public class DuplicateSessionMessage extends BaseMessage {

    private String instanceId;
    private String duplicateInstanceId;

}
