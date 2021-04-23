package com.baiyi.opscloud.domain.param.export;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/3/4 11:49 上午
 * @Since 1.0
 */
public class ExportParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "导出类型")
        private Integer taskType;

    }
}
