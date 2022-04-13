package com.baiyi.opscloud.domain.param.user;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.TagConstants;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IFilterTag;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.domain.param.SuperPageParam;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModel
    public static class UpdateUser {

        private Integer id;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "前端框架用户UUID")
        private String uuid;

        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "邮箱")
        private String email;

        private Boolean isActive;

        private String wechat;

        @ApiModelProperty(value = "手机")
        private String phone;

        @ApiModelProperty(value = "启用MFA")
        private Boolean mfa;

        private Boolean forceMfa;

        private String createdBy;

        @ApiModelProperty(value = "数据源")
        private String source;

        private String comment;

    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
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

        @ApiModelProperty(value = "初始化默认配置", example = "false")
        private Boolean needInitializeDefaultConfiguration;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "前端框架用户UUID")
        private String uuid;

        @ApiModelProperty(value = "密码")
        private String password;

        @ApiModelProperty(value = "姓名")
        private String name;

        @ApiModelProperty(value = "显示名")
        private String displayName;

        @ApiModelProperty(value = "邮箱")
        private String email;

        private final Boolean isActive = true;

        private String wechat;

        @ApiModelProperty(value = "手机")
        private String phone;

        @ApiModelProperty(value = "启用MFA")
        private final Boolean mfa = false;

        @ApiModelProperty(value = "强制启用MFA")
        private final Boolean forceMfa = false;

        private String createdBy;

        @ApiModelProperty(value = "数据源")
        private String source;

        private String comment;

    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class UserPageQuery extends PageParam implements IFilterTag, BaseBusiness.IBusinessType, IExtend {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "过滤系统标签对象")
        private Boolean filterTag;

        @ApiModelProperty(value = "展开")
        private Boolean extend;

        @ApiModelProperty(value = "有效")
        private Boolean isActive;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class EmployeeResignPageQuery extends SuperPageParam {

        private final Integer businessType = BusinessTypeEnum.USER.getType();

        private final String FILTER_SYSTEM_TAG = TagConstants.SYSTEM.getTag();

        @ApiModelProperty(value = "模糊查询")
        private String queryName;

        @ApiModelProperty(value = "过滤系统标签对象")
        private Boolean filterTag;

    }


}
