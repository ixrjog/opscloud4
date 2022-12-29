package com.baiyi.opscloud.domain.vo.auth;

import com.baiyi.opscloud.domain.vo.base.BaseVO;
import com.baiyi.opscloud.domain.vo.base.ReadableTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2022/9/19 09:36
 * @Version 1.0
 */
public class AuthPlatformVO {

    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class Platform {

        @ApiModelProperty(value = "平台ID")
        private Integer platformId;

        @ApiModelProperty(value = "平台名称")
        private String name;

        @ApiModelProperty(value = "平台说明")
        private String comment;

    }

    @EqualsAndHashCode(callSuper = true)
    @Builder
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @ApiModel
    public static class AuthPlatformLog extends BaseVO implements ReadableTime.IAgo {

        private Integer id;

        @ApiModelProperty(value = "平台ID")
        private Integer platformId;

        @ApiModelProperty(value = "平台名称")
        private String platformName;

        @ApiModelProperty(value = "认证用户名")
        private String username;

        private Boolean otp;

        @ApiModelProperty(value = "认证结果")
        private Boolean result;

        @ApiModelProperty(value = "认证消息")
        private String resultMsg;

        private String ago;

        @Override
        public Date getAgoTime() {
            return getCreateTime();
        }

    }

}
