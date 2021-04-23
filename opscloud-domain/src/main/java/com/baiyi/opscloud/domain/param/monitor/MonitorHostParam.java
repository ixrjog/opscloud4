package com.baiyi.opscloud.domain.param.monitor;

import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.server.ServerGroupParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * @Author baiyi
 * @Date 2020/11/24 10:35 上午
 * @Version 1.0
 */
public class MonitorHostParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MonitorHostPageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "监控状态")
        private Integer monitorStatus;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class CreateMonitorHost {

        @ApiModelProperty(value = "服务器id")
        @Valid
        private Integer serverId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PushMonitorHost extends CreateMonitorHost {

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class MassUpdateMonitorHostStatus extends ServerGroupParam.ServerGroupHostPatternQuery {

        @ApiModelProperty(value = "服务器分组")
        @NotBlank
        private String hostPattern;


        @ApiModelProperty(value = "主机监控状态 0或1")
        @Max(value = 1, message = "监控状态必须为0或1")
        @Min(value = 0, message = "监控状态必须为0或1")
        private Integer status;

    }

}
