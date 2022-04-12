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

    public static class UpdateUser {

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

        /**
         * 用户名
         */
        private String username;

        /**
         * 前端框架用户UUID
         */
        private String uuid;

        private String password;

        /**
         * 姓名
         */
        private String name;

        private String displayName;

        /**
         * 邮箱
         */
        private String email;
        private final Boolean isActive = true;

        private String wechat;

        /**
         * 手机
         */
        private String phone;

        /**
         * 启用MFA
         */
        private final Boolean mfa = false;

        private final Boolean forceMfa = false;

        private String createdBy;

        /**
         * 数据源
         */
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
