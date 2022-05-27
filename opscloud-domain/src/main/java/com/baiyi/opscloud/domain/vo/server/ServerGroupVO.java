package com.baiyi.opscloud.domain.vo.server;

import com.baiyi.opscloud.domain.base.IAllowOrder;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.business.BusinessDocumentVO;
import com.baiyi.opscloud.domain.vo.business.BusinessPropertyVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.UserPermissionVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @ApiModel
    public static class ServerGroup extends BaseVO implements
            ServerGroupTypeVO.IServerGroupType,
            TagVO.ITags,
            UserVO.IUserPermission,
            IAllowOrder,
            BusinessPropertyVO.IBusinessProperty,
            BusinessAssetRelationVO.IBusinessAssetRelation, // 资产与业务对象绑定关系
            BusinessDocumentVO.IBusinessDocument,
            Serializable {

        private static final long serialVersionUID = 5059407999240740609L;

        private final Integer businessType = BusinessTypeEnum.SERVERGROUP.getType();

        private BusinessDocumentVO.Document document;

        @Override
        public String getBusinessUniqueKey() {
            return name;
        }

        @ApiModelProperty(value = "组类型")
        private ServerGroupTypeVO.ServerGroupType serverGroupType;

        private List<TagVO.Tag> tags;

        @Override
        public Integer getBusinessId() {
            return id;
        }

        private UserPermissionVO.UserPermission userPermission;

        private BusinessPropertyVO.Property businessProperty;

        private Integer userId;

        @ApiModelProperty(value = "服务器数量", example = "1")
        private Integer serverSize;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "组名称")
        @NotBlank(message = "组名称不能为空")
        private String name;

        @ApiModelProperty(value = "组类型", example = "1")
        @NotNull(message = "组类型不能为空")
        private Integer serverGroupTypeId;

        @ApiModelProperty(value = "是否支持工单")
        @NotNull(message = "是否支持工单不能为空")
        private Boolean allowOrder;

        @ApiModelProperty(value = "资源描述")
        private String comment;

        private Boolean isAdmin;

        @ApiModelProperty(value = "资产id")
        private Integer assetId;

    }

}
