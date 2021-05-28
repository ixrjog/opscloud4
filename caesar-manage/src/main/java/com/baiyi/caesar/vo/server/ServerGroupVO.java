package com.baiyi.caesar.vo.server;

import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.vo.base.BaseVO;
import com.baiyi.caesar.vo.tag.TagVO;
import com.baiyi.caesar.vo.user.UserPermissionVO;
import com.baiyi.caesar.vo.user.UserVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
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

    @EqualsAndHashCode(callSuper = true)
    @Data
    @NoArgsConstructor
    @ApiModel
    public static class ServerGroup extends BaseVO implements ServerGroupTypeVO.IServerGroupType, TagVO.ITags, UserVO.IUserPermission {

        private final int businessType = BusinessTypeEnum.SERVERGROUP.getType();

        @ApiModelProperty(value = "组类型")
        private ServerGroupTypeVO.ServerGroupType serverGroupType;

        private List<TagVO.Tag> tags;

        @Override
        public int getBusinessId() {
            return id;
        }

        // UserVO.IUserPermission
        private UserPermissionVO.UserPermission userPermission;
        private Integer userId;

        @ApiModelProperty(value = "服务器数量", example = "1")
        private Integer serverSize;

        @ApiModelProperty(value = "主键", example = "1")
        private Integer id;

        @ApiModelProperty(value = "组名称")
        @NotNull(message = "组名称不能为空")
        private String name;

        @ApiModelProperty(value = "组类型", example = "1")
        @NotNull(message = "组类型不能为空")
        private Integer serverGroupTypeId;

        @ApiModelProperty(value = "是否支持工单")
        @NotNull(message = "是否支持工单不能为空")
        private Boolean allowWorkorder;

        @ApiModelProperty(value = "资源描述")
        private String comment;

    }

}
