package com.baiyi.opscloud.domain.vo.user;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 4:54 下午
 * @Version 1.0
 */
public class UserGroupVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserGroup {

        private List<UserVO.User> users;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @NotBlank(message = "用户组名称不能为空")
        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型")
        private Integer grpType;

        @ApiModelProperty(value = "允许工单申请")
        private Integer inWorkorder;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

    }
}
