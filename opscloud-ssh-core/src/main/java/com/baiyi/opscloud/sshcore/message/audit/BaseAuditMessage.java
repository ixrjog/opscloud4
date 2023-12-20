package com.baiyi.opscloud.sshcore.message.audit;

import com.baiyi.opscloud.domain.model.message.IState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/7/23 2:59 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BaseAuditMessage implements IState {

    private String state;

    private String sessionId;

    private String instanceId;

}