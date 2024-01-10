package com.baiyi.opscloud.domain.model.message;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/3/20 14:58
 * @Version 1.0
 */
@Builder
@Data
public class KubernetesDeploymentMessage implements ILoginMessage {

    private Integer applicationId;

    private Integer envType;

    private String token;

    private String messageType;

}