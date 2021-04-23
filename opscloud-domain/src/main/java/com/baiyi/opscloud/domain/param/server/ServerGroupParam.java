package com.baiyi.opscloud.domain.param.server;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

/**
 * @Author baiyi
 * @Date 2020/2/21 10:56 上午
 * @Version 1.0
 */
public class ServerGroupParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupHostPatternQuery {

        @ApiModelProperty(value = "服务器组名称")
        @NotBlank
        private String serverGroupName;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroupEnvHostPatternQuery extends ServerGroupHostPatternQuery {

        @ApiModelProperty(value = "环境类型")
        private Integer envType;

    }


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "组名")
        private String name;

        @ApiModelProperty(value = "组类型")
        private Integer grpType;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserServerGroupPageQuery extends PageParam {
        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "查询名称")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserServerGroupPermission {
        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "服务器组id")
        private Integer serverGroupId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserTicketOcServerGroupQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "工单票据id")
        private Integer workorderTicketId;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class LogMemberServerGroupQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

        @ApiModelProperty(value = "日志服务id")
        private Integer logId;

    }
}
