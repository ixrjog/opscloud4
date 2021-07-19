package com.baiyi.opscloud.sshcore.message.kubernetes;

import com.baiyi.opscloud.domain.model.message.ISessionType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/7/19 2:26 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class KubernetesCommandMessage extends BaseKubernetesMessage implements ISessionType {

    private String command;
    private String sessionType;
    private String instanceId;
}
