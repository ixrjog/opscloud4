package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/6/16 3:24 下午
 * @Version 1.0
 */
public class UserGroupParam {

    @Data
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserGroupPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "是否激活")
        private Boolean isActive;
    }
}
