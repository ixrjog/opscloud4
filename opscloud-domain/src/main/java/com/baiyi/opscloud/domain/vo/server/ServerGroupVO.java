package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.domain.vo.business.IBusinessPermissionUser;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/21 11:14 上午
 * @Version 1.0
 */
public class ServerGroupVO {

    public interface IServerGroup {
        Integer getServerGroupId();
        void setServerGroup(ServerGroup serverGroup);
    }

    @Builder
    @AllArgsConstructor
    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @Schema
    public static class ServerGroup extends BaseVO implements
            ServerGroupTypeVO.IServerGroupType,
            TagVO.ITags,
            UserVO.IUserPermission,
            IAllowOrder,
            BusinessPropertyVO.IBusinessProperty,
            // 资产与业务对象绑定关系
            BusinessAssetRelationVO.IBusinessAssetRelation,
            BusinessDocumentVO.IBusinessDocument,
            IBusinessPermissionUser,
            Serializable {

        @Serial
        private static final long serialVersionUID = 5059407999240740609L;

        @Schema(description = "授权用户")
        private List<UserVO.User> users;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private BusinessDocumentVO.Document document;

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }

        @Schema(description = "组类型")
        private ServerGroupTypeVO.ServerGroupType serverGroupType;

        private List<TagVO.Tag> tags;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private UserPermissionVO.UserPermission userPermission;

        private BusinessPropertyVO.Property businessProperty;

        private Integer userId;

        @Schema(description = "服务器数量", example = "1")
        private Integer serverSize;

        @Schema(description = "主键", example = "1")
        private Integer id;

        @Schema(description = "组名称")
        private String name;

        @Schema(description = "组类型", example = "1")
        private Integer serverGroupTypeId;

        @Schema(description = "是否支持工单")
        private Boolean allowOrder;

        @Schema(description = "资源描述")
        private String comment;

        private Boolean isAdmin;

        @Schema(description = "资产id")
        private Integer assetId;

    }

}