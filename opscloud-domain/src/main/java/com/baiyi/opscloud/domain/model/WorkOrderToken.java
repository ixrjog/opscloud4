package com.baiyi.opscloud.domain.model;

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
        private static final long serialVersionUID = -1L;

        private Integer ticketId;

        private Integer applicationId;

        @Override
        public Integer getKey() {
            return this.applicationId;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class LeoDeployToken implements IWorkOrderToken, Serializable {

        @Serial
        private static final long serialVersionUID = -1L;

        private Integer buildId;

        private Integer applicationId;

        @Override
        public Integer getKey() {
            return this.buildId;
        }

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SerDeployToken implements IWorkOrderToken, Serializable {

        @Serial
        private static final long serialVersionUID = -1L;

        private Integer ticketId;

        private Integer serDeployTaskId;

        @Override
        public Integer getKey() {
            return this.serDeployTaskId;
        }
    }

}
