package com.baiyi.caesar.domain.vo.user;

import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.domain.vo.base.IWorkorder;
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


    public interface IUserGroups {
        Integer getUserId();

        void setUserGroups(List<UserGroupVO.UserGroup> userGroups);

    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserGroup implements UserVO.IUserPermission,IWorkorder {

        private final int businessType = BusinessTypeEnum.USERGROUP.getType();

        @Override
        public int getBusinessId() {
            return id;
        }

        // UserVO.IUserPermission
        private UserPermissionVO.UserPermission userPermission;

        private Integer userId;

        private List<UserVO.User> users;

        @ApiModelProperty(value = "授权用户数量", example = "1")
        private Integer userSize;

        @ApiModelProperty(value = "主键")
        private Integer id;

        @NotBlank(message = "用户组名称不能为空")
        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型")
        private Integer groupType;

        @ApiModelProperty(value = "允许工单申请")
        private Boolean allowWorkorder;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

    }
}
