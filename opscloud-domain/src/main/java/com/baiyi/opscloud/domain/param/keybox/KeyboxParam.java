package com.baiyi.opscloud.domain.param.keybox;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/5/21 11:20 上午
 * @Version 1.0
 */
public class KeyboxParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "查询系统账户")
        private String querySystemUser;

    }
}
