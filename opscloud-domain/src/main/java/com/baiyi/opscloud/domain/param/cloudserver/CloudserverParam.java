package com.baiyi.opscloud.domain.param.cloudserver;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;


public class CloudserverParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "云主机类型")
        private Integer cloudserverType;

        @ApiModelProperty(value = "服务器名称")
        private String serverName;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        @ApiModelProperty(value = "服务器状态")
        private Integer serverStatus;
    }


}
