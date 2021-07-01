package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class UserParam {

    @Data
    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @NoArgsConstructor
    @ApiModel
    public static class UserPageQuery extends PageParam implements IExtend {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "是否激活")
        private Boolean isActive;
    }


}
