package com.baiyi.opscloud.loop.kubernetes;

import com.baiyi.opscloud.common.util.JSONUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/3/20 17:50
 * @Version 1.0
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class KubernetesDeploymentResponse<T> {

    private T body;

    private String messageType;

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }

}