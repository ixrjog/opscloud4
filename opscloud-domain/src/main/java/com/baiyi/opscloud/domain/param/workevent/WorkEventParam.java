package com.baiyi.opscloud.domain.param.workevent;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author 修远
 * @Date 2022/8/9 3:24 PM
 * @Since 1.0
 */
public class WorkEventParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        private Integer workRoleId;

    }
}
