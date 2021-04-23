package com.baiyi.opscloud.domain.param.fault;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 4:22 下午
 * @Since 1.0
 */
public class FaultParam {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class InfoPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "故障级别")
        private String faultLevel;

        @ApiModelProperty(value = "原因归类id")
        private Integer rootCauseTypeId;

        @ApiModelProperty(value = "查询年")
        private Integer queryYear;

        @ApiModelProperty(value = "查询月")
        private Integer queryMonth;

    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class RootCauseTypePageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class AddRootCauseType{

        @NotNull
        @ApiModelProperty(value = "原因归类")
        private String rootCauseType;
    }

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class ActionPageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "action状态")
        private Integer actionStatus;

    }
}
