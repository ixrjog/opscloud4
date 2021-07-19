package com.baiyi.opscloud.sshcore.message.server;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/12 10:44 上午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class ResizeMessage extends BaseServerMessage {

    private String instanceId;
}
