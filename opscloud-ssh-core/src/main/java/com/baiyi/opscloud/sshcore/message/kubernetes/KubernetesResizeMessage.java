package com.baiyi.opscloud.sshcore.message.kubernetes;

import com.baiyi.opscloud.domain.model.message.ISessionType;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/7/16 3:19 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class KubernetesResizeMessage extends BaseKubernetesMessage implements ISessionType {

    private KubernetesResource data;
    private String sessionType;
    private String instanceId;

}