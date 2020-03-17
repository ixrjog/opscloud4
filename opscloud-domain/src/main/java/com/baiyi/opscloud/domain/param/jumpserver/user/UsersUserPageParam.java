package com.baiyi.opscloud.domain.param.jumpserver.user;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/3/12 10:07 上午
 * @Version 1.0
 */
public class UsersUserPageParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "扩展属性",example = "0")
        private Integer extend;

        @ApiModelProperty(value = "是否管理员",example = "1")
        private Integer isAdmin;

        @ApiModelProperty(value = "用户是否有效",example = "0")
        private Integer isActive;
    }
}
