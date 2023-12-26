package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IFilterTag;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * @Author baiyi
 * @Date 2020/2/20 9:43 上午
 * @Version 1.0
 */
public class UserParam {

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UpdateUser {

        private Integer id;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "前端框架用户UUID")
        private String uuid;

        @Schema(description = "密码")
        private String password;

        @Schema(description = "姓名")
        private String name;

        @Schema(description = "显示名")
        private String displayName;

        @Schema(description = "邮箱")
        private String email;

        private Boolean isActive;

        private String wechat;

        @Schema(description = "手机")
        private String phone;

        @Schema(description = "启用MFA")
        private Boolean mfa;

        private Boolean forceMfa;

        private String createdBy;

        @Schema(description = "数据源")
        private String source;

        private String comment;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class CreateUser implements BusinessAssetRelationVO.IBusinessAssetRelation {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        @Override
        public Integer getBusinessId() {
            return id;
        }

        @Override
        public String getBusinessUniqueKey() {
            return username;
        }

        private Integer assetId;

        private Integer id;

        @Schema(description = "初始化默认配置", example = "false")
        private Boolean needInitializeDefaultConfiguration;

        @Schema(description = "用户名")
        private String username;

        @Schema(description = "前端框架用户UUID")
        private String uuid;

        @Schema(description = "密码")
        private String password;

        @Schema(description = "姓名")
        private String name;

        @Schema(description = "显示名")
        private String displayName;

        @Schema(description = "邮箱")
        private String email;

        private final Boolean isActive = true;

        private String wechat;

        @Schema(description = "手机")
        private String phone;

        @Schema(description = "启用MFA")
        private final Boolean mfa = false;

        @Schema(description = "强制启用MFA")
        private final Boolean forceMfa = false;

        private String createdBy;

        @Schema(description = "数据源")
        private String source;

        private String comment;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class UserPageQuery extends PageParam implements IFilterTag, BaseBusiness.IBusinessType, IExtend {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @Schema(description = "模糊查询")
        private String queryName;

        @Schema(description = "过滤系统标签对象")
        private Boolean filterTag;

        @Schema(description = "展开")
        private Boolean extend;

        @Schema(description = "有效")
        private Boolean isActive;

        @Schema(description = "标签ID")
        private Integer tagId;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @Schema
    public static class EmployeeResignPageQuery extends SuperPageParam {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @Schema(description = "模糊查询")
        private String queryName;

        @Schema(description = "过滤系统标签对象")
        private Boolean filterTag;

    }

}