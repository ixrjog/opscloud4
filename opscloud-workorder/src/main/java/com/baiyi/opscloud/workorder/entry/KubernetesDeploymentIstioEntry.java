package com.baiyi.opscloud.workorder.entry;

import com.baiyi.opscloud.common.util.JSONUtil;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/12/7 13:23
 * @Version 1.0
 */
public class KubernetesDeploymentIstioEntry {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KubernetesDeployment implements Serializable {

        @Serial
        private static final long serialVersionUID = 1061046714107482211L;

        @Schema(description = "数据实例UUID")
        private String instanceUuid;

        @Schema(description = "namespace:deployment")
        private String name;

        private String namespace;

        private String deploymentName;

        @Schema(description = "Istio")
        private String istioInject;

        private Integer id;

        private String comment;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}