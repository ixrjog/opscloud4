package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;


public class CloudServerParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudServerPageQuery extends PageParam {

        @ApiModelProperty(value = "云主机类型")
        private Integer cloudServerType;

        @ApiModelProperty(value = "服务器名称")
        private String serverName;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        @ApiModelProperty(value = "服务器状态")
        private Integer serverStatus;
    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CloudServerChargePageQuery extends CloudServerPageQuery {

        @ApiModelProperty(value = "付费类型")
        private String chargeType;

        @ApiModelProperty(value = "续费类型")
        private String renewalStatus;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class DeleteInstance {

        @ApiModelProperty(value = "云主机类型")
        @NotBlank(message = "云主机类型不能为空")
        private String key;

        @ApiModelProperty(value = "云主机实例id")
        @NotBlank(message = "云主机实例不能为空")
        private String instanceId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PowerAction {

        @ApiModelProperty(value = "云主机类型")
        @NotBlank(message = "云主机类型不能为空")
        private String key;

        @ApiModelProperty(value = "云主机实例id")
        @NotBlank(message = "云主机实例不能为空")
        private String instanceId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class  ModifyInstanceChargeType {

        @ApiModelProperty(value = "云主机类型")
        @NotBlank(message = "云主机类型不能为空")
        private String key;

        @ApiModelProperty(value = "云主机实例id")
        @NotBlank(message = "云主机实例不能为空")
        private String instanceId;

        @ApiModelProperty(value = "实例需要修改的目标计费方式")
        @NotBlank(message = "实例需要修改的目标计费方式不能为空")
        private String chargeType;
    }



}
