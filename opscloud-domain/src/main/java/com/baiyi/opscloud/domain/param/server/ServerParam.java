package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/2/21 4:48 下午
 * @Version 1.0
 */
public class ServerParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerPageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询专用")
        private String queryName;

        @ApiModelProperty(value = "服务器名")
        private String name;

        @ApiModelProperty(value = "查询ip")
        private String queryIp;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;

        @ApiModelProperty(value = "状态")
        private Integer serverStatus;

        @ApiModelProperty(value = "标签id")
        private Integer tagId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryByServerGroup {

        @ApiModelProperty(value = "服务器组id，优先级高", example = "1")
        private Integer serverGroupId;

        @ApiModelProperty(value = "服务器组名称，优先级低")
        private String serverGroupName;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class QueryByServerIds {

        @ApiModelProperty(value = "服务器id列表")
        private Set<Integer> ids;

    }
}
