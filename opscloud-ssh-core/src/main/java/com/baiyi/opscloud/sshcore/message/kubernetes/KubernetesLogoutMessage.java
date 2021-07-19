package com.baiyi.opscloud.sshcore.message.kubernetes;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2021/7/16 5:18 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class KubernetesLogoutMessage  extends BaseKubernetesMessage{

    private String instanceId;
}
