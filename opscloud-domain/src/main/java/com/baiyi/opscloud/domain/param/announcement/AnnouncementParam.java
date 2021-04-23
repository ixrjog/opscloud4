package com.baiyi.opscloud.domain.param.announcement;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/22 4:08 下午
 * @Since 1.0
 */
public class AnnouncementParam {

    @Data
    @ApiModel
    @NoArgsConstructor
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "关键字查询")
        private String queryName;

    }
}
