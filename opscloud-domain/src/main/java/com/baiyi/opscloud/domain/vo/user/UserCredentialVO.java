package com.baiyi.opscloud.domain.vo.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/2/27 1:17 下午
 * @Version 1.0
 */
public class UserCredentialVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UserCredential {
        @ApiModelProperty(value = "主键")
        private Integer id;

        @ApiModelProperty(value = "用户id")
        private Integer userId;

        @ApiModelProperty(value = "用户名")
        private String username;

        @ApiModelProperty(value = "标题")
        private String title;

        @ApiModelProperty(value = "凭据类型")
        private Integer credentialType;

        @ApiModelProperty(value = "凭据内容")
        private String credential;

        @ApiModelProperty(value = "凭据指纹")
        private String fingerprint;

        @ApiModelProperty(value = "创建时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date createTime;

        @ApiModelProperty(value = "更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date updateTime;
    }

}
