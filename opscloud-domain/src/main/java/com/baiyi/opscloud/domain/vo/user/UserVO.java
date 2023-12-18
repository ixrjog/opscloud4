package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.annotation.DesensitizedField;
import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.SensitiveTypeEnum;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;
import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.domain.vo.user.mfa.MfaVO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/20 11:20 上午
 * @Version 1.0
 */
public class UserVO {

    public interface IUsername {
        String getUsername();
    }

    public interface IUser extends IUsername {
        void setUser(UserVO.User user);
    }

    public interface IUserPermission extends BaseBusiness.IBusiness {
        void setUserPermission(UserPermissionVO.UserPermission userPermission);

        Integer getUserId();
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class RamUser {

        @Schema(description = "实例名称")
        private String instanceName;

        @Schema(description = "实例UUID")
        private String instanceUuid;

        private String name;

        private String username;

        @Schema(description = "登录用户(username@company-alias): https://help.aliyun.com/document_detail/143060.html")
        private String loginUser;

        @Schema(description = "登录地址")
        private String loginUrl;

        @Schema(description = "AccessKey列表")
        private List<DsAssetVO.Asset> accessKeys;

        @Schema(description = "策略列表")
        private List<DsAssetVO.Asset> ramPolicies;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class User extends BaseVO implements
            BusinessAssetRelationVO.IBusinessAssetRelation, // 资产与业务对象绑定关系
            TagVO.ITags,
            IUserPermission,
            AuthRoleVO.IRoles {

        private List<TagVO.Tag> tags;

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Override
        public Integer getUserId() {
            return id;
        }

        @Schema(description = "资产id")
        private Integer assetId;

        private List<AuthRoleVO.Role> roles;

        @Schema(description = "头像")
        private String avatar;

        @Schema(description = "用户凭证")
        private UserCredentialVO.CredentialDetails credentialDetails;

        @Schema(description = "有效的AccessToken")
        private List<AccessTokenVO.AccessToken> accessTokens;

        private Map<String, List<IUserPermission>> businessPermissions;

        @Schema(description = "云AM账户Map")
        @Builder.Default
        private Map<String, List<AccessManagementVO.XAccessManagement>> amMap = Maps.newHashMap();

        @Schema(description = "云AM用户列表（某一类型）")
        @Builder.Default
        private List<AccessManagementVO.XAccessManagement> ams = Lists.newArrayList();

        // 废弃
        @Schema(description = "阿里云RAM用户列表")
        @Builder.Default
        private List<RamUser> ramUsers = Lists.newArrayList();

        @Schema(description = "主键")
        @Builder.Default
        private Integer id = 0;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "前端框架专用")
        private String uuid;

        @DesensitizedField(type = SensitiveTypeEnum.PASSWORD)
        @Schema(description = "密码")
        private String password;

        @Schema(description = "显示名")
        private String displayName;

        @Schema(description = "姓名")
        private String name;

        @Email
        @Schema(description = "邮箱")
        private String email;

        @Schema(description = "有效用户")
        @NotNull(message = "有效用户字段不能为空")
        @Builder.Default
        private Boolean isActive = true;

        @Schema(description = "最后登录时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastLogin;

        @Schema(description = "微信")
        private String wechat;

        // @DesensitizedField(type = SensitiveTypeEnum.MOBILE_PHONE)
        @Schema(description = "手机")
        private String phone;

        @Schema(description = "启用MFA")
        private Boolean mfa;

        @Schema(description = "管理员强制启用MFA")
        private Boolean forceMfa;

        @Schema(description = "创建者")
        private String createdBy;

        @Schema(description = "数据源")
        private String source;

        @Schema(description = "留言")
        private String comment;

        @Override
        public String getBusinessUniqueKey() {
            return username;
        }

        private UserPermissionVO.UserPermission userPermission;
    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserMFA {

        private Integer id;

        private String username;

        @Schema(description = "用户是否启用MFA")
        private Boolean mfa;

        @Schema(description = "是否强制用户启用MFA")
        private Boolean forceMfa;

        @Schema(description = "用户MFA配置")
        private MfaVO.MFA userMfa;

    }

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserIAMMFA {

        private Integer id;

        private String username;

        @Schema(description = "用户是否启用MFA")
        private Boolean mfa;

        @Schema(description = "用户MFA配置")
        private List<MfaVO.MFA> userMfas;

    }

}