package com.baiyi.opscloud.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/26 6:05 下午
 * @Version 1.0
 */
public class UserApiTokenVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserApiToken {

        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "是否有效")
        private Boolean valid;

        @ApiModelProperty(value = "登录唯一标识")
        private String token;

        @ApiModelProperty(value = "令牌id")
        private String tokenId;

        @ApiModelProperty(value = "过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date expiredTime;

        @ApiModelProperty(value = "留言")
        private String comment;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }
}
