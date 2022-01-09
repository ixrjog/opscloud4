package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserGroup implements UserVO.IUserPermission, IAllowOrder, BusinessAssetRelationVO.IBusinessAssetRelation {

        private final Integer businessType = BusinessTypeEnum.USERGROUP.getType();

        @ApiModelProperty(value = "资产id")
        private Integer assetId;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private UserPermissionVO.UserPermission userPermission;

        private Integer userId;

        private List<UserVO.User> users;

        @ApiModelProperty(value = "授权用户数量", example = "1")
        private Integer userSize;

        @ApiModelProperty(value = "主键")
        @Builder.Default
        private Integer id = 0;

        @NotBlank(message = "用户组名称不能为空")
        @ApiModelProperty(value = "用户组名称")
        private String name;

        @ApiModelProperty(value = "用户组类型")
        @Builder.Default
        private Integer groupType = 0;

        @ApiModelProperty(value = "允许工单申请")
        private Boolean allowOrder;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }
    }
}
