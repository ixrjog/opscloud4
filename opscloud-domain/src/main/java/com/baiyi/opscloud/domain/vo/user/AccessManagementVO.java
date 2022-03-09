package com.baiyi.opscloud.domain.vo.user;

import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/8 10:59 AM
 * @Version 1.0
 */
public class AccessManagementVO {

    /**
     * AWS IAM
     * Identity and Access Management
     */
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @Data
    @ApiModel
    public static class IAM extends XAccessManagement implements Serializable {

        private static final long serialVersionUID = -5541945548323066174L;
        
        private final String type = DsAssetTypeConstants.IAM_USER.name();

    }

    /**
     * Aliyun RAM
     * Resource Access Management
     */
    @EqualsAndHashCode(callSuper = true)
    @SuperBuilder(toBuilder = true)
    @Data
    @ApiModel
    public static class RAM extends XAccessManagement implements Serializable {

        private static final long serialVersionUID = -2175343102360414610L;
        private final String type = DsAssetTypeConstants.RAM_USER.name();

    }

    @SuperBuilder(toBuilder = true)
    @Data
    @ApiModel
    public static class XAccessManagement implements Serializable {

        private static final long serialVersionUID = -8444786214672507521L;
        private String type;

        @ApiModelProperty(value = "实例名称")
        private String instanceName;

        @ApiModelProperty(value = "实例UUID")
        private String instanceUuid;

        private String name;

        private String username;

        @ApiModelProperty(value = "登录用户")
        private String loginUser;

        @ApiModelProperty(value = "登录地址")
        private String loginUrl;

        @ApiModelProperty(value = "AccessKey列表")
        private List<DsAssetVO.Asset> accessKeys;

        @ApiModelProperty(value = "策略列表")
        private List<DsAssetVO.Asset> policies;

    }

}
