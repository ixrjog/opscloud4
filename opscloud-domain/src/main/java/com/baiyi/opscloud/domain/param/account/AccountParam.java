package com.baiyi.opscloud.domain.param.account;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/**
 * @Author baiyi
 * @Date 2020/12/9 11:08 上午
 * @Version 1.0
 */
public class AccountParam {


    @Data
    @NoArgsConstructor
    @ApiModel
    public static class AccountPageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "账户类型")
        @NonNull
        private Integer accountType;
    }
}
