package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2022/2/10 6:18 PM
 * @Version 1.0
 */
public class UserAmParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateUser implements DsInstanceVO.IInstance {
        @Schema(description = "数据源实例UUID")
        private String instanceUuid;

        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "用户名")
        private String username;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class GrantPolicy implements DsInstanceVO.IInstance {
        @Schema(description = "数据源实例UUID")
        private String instanceUuid;
        @Schema(description = "数据源实例ID")
        private Integer instanceId;
        //        @Schema(description = "需要创建账户")
        //        private Boolean needCreate;
        private Policy policy;
        @Schema(description = "用户名")
        private String username;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @Schema
    public static class RevokePolicy extends GrantPolicy {
    }

    @Builder
    @Data
    public static class Policy {
        private String policyName;
        private String policyType;
        private String policyArn; // IAM专用
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateLoginProfile implements DsInstanceVO.IInstance {

        @Schema(description = "数据源实例UUID")
        private String instanceUuid;

        @Schema(description = "数据源实例ID")
        private Integer instanceId;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "密码")
        private String password;

    }

}