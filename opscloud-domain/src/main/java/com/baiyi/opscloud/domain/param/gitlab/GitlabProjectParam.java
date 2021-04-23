package com.baiyi.opscloud.domain.param.gitlab;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2020/7/20 2:14 下午
 * @Version 1.0
 */
public class GitlabProjectParam {

    @Data
    @Builder
    @ApiModel
    public static class GitlabProjectPageQuery extends PageParam {

        @ApiModelProperty(value = "实例id", example = "1")
        private Integer instanceId;

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性", example = "1")
        private Integer extend;

    }
}
