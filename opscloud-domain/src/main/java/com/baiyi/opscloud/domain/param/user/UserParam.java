package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class UserParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性",example = "0")
        private Integer extend;

        @ApiModelProperty(value = "用户名")
        private String username;

    }
}
