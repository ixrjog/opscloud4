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
 * @Date 2022/7/18 13:56
 * @Version 1.0
 */
public class ApplicationScaleReplicasEntry {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class KubernetesDeployment implements Serializable {

        @Serial
        private static final long serialVersionUID = -2057324997536563597L;

        @Schema(description = "数据实例UUID")
        private String instanceUuid;

        @Schema(description = "namespace:deployment")
        private String name;

        private String namespace;

        private String deploymentName;

        @Schema(description = "原副本数量")
        private Integer replicas;

        @Schema(description = "扩容后的副本数量")
        private Integer scaleReplicas;

        private Integer id;

        private String comment;

        @Override
        public String toString() {
            return JSONUtil.writeValueAsString(this);
        }
    }

}