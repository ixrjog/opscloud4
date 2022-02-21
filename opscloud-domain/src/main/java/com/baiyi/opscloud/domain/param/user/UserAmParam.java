package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class CreateUser implements DsInstanceVO.IInstance {
        @ApiModelProperty(value = "数据源实例UUID")
        private String instanceUuid;

        @ApiModelProperty(value = "数据源实例ID")
        private Integer instanceId;

        @ApiModelProperty(value = "用户名")
        private String username;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class GrantPolicy implements DsInstanceVO.IInstance {
        @ApiModelProperty(value = "数据源实例UUID")
        private String instanceUuid;
        @ApiModelProperty(value = "数据源实例ID")
        private Integer instanceId;
        //        @ApiModelProperty(value = "需要创建账户")
//        private Boolean needCreate;
        private Policy policy;
        @ApiModelProperty(value = "用户名")
        private String username;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @ApiModel
    public static class RevokePolicy extends GrantPolicy {
    }

    @Builder
    @Data
    public static class Policy {
        private String policyName;
        private String policyType;
        private String policyArn; // IAM专用
    }
}
