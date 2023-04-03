package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    @Schema
    public static class UserGroup implements UserVO.IUserPermission, IAllowOrder, BusinessAssetRelationVO.IBusinessAssetRelation {

        private final Integer businessType = BusinessTypeEnum.USERGROUP.getType();

        @Schema(name = "资产id")
        private Integer assetId;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private UserPermissionVO.UserPermission userPermission;

        private Integer userId;

        private List<UserVO.User> users;

        @Schema(name = "授权用户数量", example = "1")
        private Integer userSize;

        @Schema(name = "主键")
        @Builder.Default
        private Integer id = 0;

        @NotBlank(message = "用户组名称不能为空")
        @Schema(name = "用户组名称")
        private String name;

        @Schema(name = "用户组类型")
        @Builder.Default
        private Integer groupType = 0;

        @Schema(name = "允许工单申请")
        private Boolean allowOrder;

        @Schema(name = "数据源")
        private String source;

        @Schema(name = "留言")
        private String comment;

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }
    }
}
