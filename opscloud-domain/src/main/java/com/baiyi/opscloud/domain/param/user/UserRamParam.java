package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/12/12 4:05 PM
 * @Version 1.0
 */
public class UserRamParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class CreateRamUser implements DsInstanceVO.IInstance {
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
    public static class GrantRamPolicy implements DsInstanceVO.IInstance {
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
    public static class RevokeRamPolicy extends GrantRamPolicy {
    }

    @Builder
    @Data
    public static class Policy {
        private String policyName;
        private String policyType;
    }


}
