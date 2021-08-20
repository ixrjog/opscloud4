package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:20 上午
 * @Version 1.0
 */
public class UserVO {

    public interface IUser {

        String getUsername();

        void setUser(UserVO.User user);
    }


    public interface IUserPermission extends BaseBusiness.IBusiness {

        void setUserPermission(UserPermissionVO.UserPermission userPermission);
        Integer getUserId();
    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class User extends BaseVO implements BusinessAssetRelationVO.IBusinessAssetRelation // 资产与业务对象绑定关系
    {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @ApiModelProperty(value = "资产id")
        private Integer assetId;

        private List<AuthRoleVO.Role> roles;

        @ApiModelProperty(value = "用户凭证")
        private UserCredentialVO.CredentialDetails credentialDetails;

        @ApiModelProperty(value = "有效的AccessToken")
        private List<AccessTokenVO.AccessToken> accessTokens;

        private Map<String, List<IUserPermission>> businessPermissions;

        @ApiModelProperty(value = "主键")
        @Builder.Default
        private Integer id = 0;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "前端框架专用")
        private String uuid;

        @DesensitizedField(type = SensitiveTypeEnum.PASSWORD)
        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "姓名")
        private String name;

        @Email
        @ApiModelProperty(value = "邮箱")
        private String email;

        @ApiModelProperty(value = "有效用户")
        @NotNull(message = "有效用户字段不能为空")
        @Builder.Default
        private Boolean isActive = true;

        @ApiModelProperty(value = "最后登录时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastLogin;

        @ApiModelProperty(value = "微信")
        private String wechat;

        @DesensitizedField(type = SensitiveTypeEnum.MOBILE_PHONE)
        @ApiModelProperty(value = "手机")
        private String phone;

        @ApiModelProperty(value = "创建者")
        private String createdBy;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "留言")
        private String comment;

        @Override
        public String getBusinessUniqueKey() {
            return username;
        }
    }

}
