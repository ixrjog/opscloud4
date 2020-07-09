package com.baiyi.opscloud.domain.param.profile;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:00 上午
 * @Version 1.0
 */
public class ProfileSubscriptionParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "查询名称")
        private String queryName;

    }

}
