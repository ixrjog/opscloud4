package com.baiyi.opscloud.domain.vo.finops;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2023/8/29 11:23
 * @Version 1.0
 */
public class KubernetesFinOpsVO {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class FinOpsReport implements Serializable {

        @Serial
        private static final long serialVersionUID = -8445215941315982331L;

        public static final FinOpsReport EMPTY = FinOpsReport.builder().build();

        public void sort() {
            Collections.sort(this.data);
        }

        @Schema(description = "FinOps Data")
        @Builder.Default
        private List<FinOpsData> data = Collections.emptyList();

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class FinOpsData implements Comparable<FinOpsData>, Serializable {

        @Serial
        private static final long serialVersionUID = 6602738677515278417L;

        @Schema(description = "FinOps Tag")
        private String tag;

        @Schema(description = "FinOps Desc")
        private String desc;

        @Schema(description = "Replica Total")
        private Integer replicaTotal;

        @Schema(description = "Rate")
        private Double rate;

        @Schema(description = "Display Rate")
        private String displayRate;

        @Override
        public int compareTo(FinOpsData o) {
            return o.getReplicaTotal() - this.getReplicaTotal();
        }
    }

}