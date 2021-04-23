package com.baiyi.opscloud.domain.vo.cloud;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/9 4:25 下午
 * @Since 1.0
 */
public class AliyunDomainVO {

    @Data
    @ApiModel
    public static class Domain {

        private Integer id;

        @ApiModelProperty(value = "域名")
        private String domainName;

        @ApiModelProperty(value = "实例编号")
        private String instanceId;

        /**
         * 域名实名认证状态。取值：
         * <p>
         * FAILED：实名认证失败。
         * SUCCEED：实名认证成功。
         * NONAUDIT：未实名认证。
         * AUDITING：审核中。
         */
        @ApiModelProperty(value = "域名实名认证状态")
        private String domainAuditStatus;

        @ApiModelProperty(value = "域名分组编号")
        private String domainGroupId;

        @ApiModelProperty(value = "域名分组名称")
        private String domainGroupName;

        /**
         * 域名状态。取值：
         * <p>
         * 1：急需续费。
         * 2：急需赎回。
         * 3：正常。
         */
        @ApiModelProperty(value = "域名状态")
        private String domainStatus;

        /**
         * 域名类型。取值：
         * New gTLD。
         * gTLD。
         * ccTLD。
         */
        @ApiModelProperty(value = "域名类型")
        private String domainType;

        @ApiModelProperty(value = "域名到期日和当前的时间的天数差值")
        private Integer expirationCurrDateDiff;

        @ApiModelProperty(value = "域名到期日期")
        private String expirationDate;

        /**
         * 域名过期状态。取值：
         * 1：域名未过期。
         * 2：域名已过期。
         */
        @ApiModelProperty(value = "服务器组信息")
        private String expirationDateStatus;

        @ApiModelProperty(value = "产品ID")
        private String productId;

        /**
         * 域名注册类型。取值：
         * 1：个人。
         * 2：企业。
         */
        @ApiModelProperty(value = "域名注册类型")
        private String registrantType;

        @ApiModelProperty(value = "注册时间")
        private String registrationDate;

        @ApiModelProperty(value = "域名备注")
        private String remark;

        private Integer isActive;
    }
}
