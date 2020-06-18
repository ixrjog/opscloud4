package com.baiyi.opscloud.domain.param.cloud;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

/**
 * @Author baiyi
 * @Date 2020/6/15 9:18 上午
 * @Version 1.0
 */
public class AliyunLogMemberParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {
        @NotNull
        private Integer logId;

        @ApiModelProperty(value = "查询关键字")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AddLogMember {
        @NotNull
        private Integer logId;
        @NotNull
        private Integer serverGroupId;
        private String topic;
        private String comment;
    }
}
