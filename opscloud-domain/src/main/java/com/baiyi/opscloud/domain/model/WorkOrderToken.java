package com.baiyi.opscloud.domain.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @Author baiyi
 * @Date 2023/6/16 11:38
 * @Version 1.0
 */
public class WorkOrderToken {

    public interface IWorkOrderToken {

        Integer getKey();

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ApolloReleaseToken implements IWorkOrderToken, Serializable {

        @Serial
        private static final long serialVersionUID = 2854611198344283355L;

        private Integer ticketId;

        @Schema(description = "applicationId")
        private Integer key;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeoDeployToken implements IWorkOrderToken, Serializable {

        @Serial
        private static final long serialVersionUID = -8259194390193881090L;

        @Schema(description = "buildId")
        private Integer key;

        private Integer applicationId;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SerDeployToken implements IWorkOrderToken, Serializable {

        @Serial
        private static final long serialVersionUID = 4019009610938340429L;

        private Integer ticketId;

        @Schema(description = "serDeployTaskId")
        private Integer key;

    }

}