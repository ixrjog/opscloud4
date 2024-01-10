package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.v3.oas.annotations.media.Schema;
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

        @Schema(description = "资产ID")
        private Integer assetId;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private UserPermissionVO.UserPermission userPermission;

        private Integer userId;

        private List<UserVO.User> users;

        @Schema(description = "授权用户数量", example = "1")
        private Integer userSize;

        @Schema(description = "主键")
        @Builder.Default
        private Integer id = 0;

        @Schema(description = "用户组名称")
        private String name;

        @Schema(description = "用户组类型")
        @Builder.Default
        private Integer groupType = 0;

        @Schema(description = "允许工单申请")
        private Boolean allowOrder;

        @Schema(description = "数据源")
        private String source;

        @Schema(description = "留言")
        private String comment;

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }
    }

}