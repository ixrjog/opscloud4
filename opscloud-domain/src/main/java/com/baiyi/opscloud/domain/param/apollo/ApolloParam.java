package com.baiyi.opscloud.domain.param.apollo;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/5/30 14:06
 * @Version 1.0
 */
public class ApolloParam {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ReleaseEvent {

        private String appId;

        @NotBlank(message = "环境不能为空")
        private String env;

        private String username;

        private String clusterName;

        private String namespaceName;

        private String branchName;

        private String token;

        private Boolean isGray;

        // NamespaceRelease
        private String releaseTitle;
        private String releaseComment;
        private String releasedBy;
        private boolean isEmergencyPublish;

    }

}
