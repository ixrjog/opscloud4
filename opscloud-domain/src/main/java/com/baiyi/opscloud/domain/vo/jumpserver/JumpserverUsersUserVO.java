package com.baiyi.opscloud.domain.vo.jumpserver;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/12 10:13 上午
 * @Version 1.0
 */
public class JumpserverUsersUserVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class UsersUser{

        private List<JumpserverUsersUsergroupVO.UsersUsergroup> usersUsergroups;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date lastLogin;

        private String firstName;

        private String lastName;

        private Boolean isActive;

        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date dateJoined;

        @ApiModelProperty(value = "主键",example="格式为uuid")
        private String id;

        private String username;

        private String name;

        private String email;

        private String role;

        private String avatar;

        private String wechat;

        private String phone;

        private Boolean isFirstLogin;

        @ApiModelProperty(value = "账户过期时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date dateExpired;

        @ApiModelProperty(value = "createdBy")
        private String createdBy;

        @ApiModelProperty(value = "MFA级别")
        private Short mfaLevel;

//        @ApiModelProperty(value = "OTP密钥")
//        private String otpSecretKey;

        @ApiModelProperty(value = "数据源")
        private String source;

        @ApiModelProperty(value = "用户密码更新时间")
        @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd HH:mm:ss")
        private Date datePasswordLastUpdated;

//        @ApiModelProperty(value = "用户私钥")
//        private String privateKey;

        @ApiModelProperty(value = "用户公钥")
        private String publicKey;

        @ApiModelProperty(value = "资源描述")
        private String comment;


    }
}
