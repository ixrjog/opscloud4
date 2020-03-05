package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.param.PageParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:50 下午
 * @Version 1.0
 */
public class UserGroupParam {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class PageQuery extends PageParam {

        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型", example = "1")
        private Integer grpType;

        @ApiModelProperty(value = "允许工作流申请", example = "1")
        private Integer workflow;

        @ApiModelProperty(value = "扩展属性", example = "0")
        private Integer extend;

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserUserGroupPageQuery extends PageParam {
        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "查询名称")
        private String queryName;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserUserGroupPermission {
        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "用户组id")
        private Integer userGroupId;
    }

}
