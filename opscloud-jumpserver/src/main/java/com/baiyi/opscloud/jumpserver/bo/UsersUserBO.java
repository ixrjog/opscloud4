package com.baiyi.opscloud.jumpserver.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/3/8 5:41 下午
 * @Version 1.0
 */
@Data
@Builder
public class UsersUserBO {

    @Builder.Default
    private String password = "";
    private Date lastLogin;
    @Builder.Default
    private String firstName = "";
    @Builder.Default
    private String lastName = "";
    @Builder.Default
    private Boolean isActive = true;
    @Builder.Default
    private Date dateJoined = new Date();
    private String id;
    private String username;
    @Builder.Default
    private String name ="";
    private String email;
    @Builder.Default
    private String role = "User";
    private String avatar;
    @Builder.Default
    private String wechat = "";
    private String phone;
    @Builder.Default
    private String privateKey = "";
    @Builder.Default
    private String publicKey = "";
    @Builder.Default
    private Boolean isFirstLogin = true;
    private Date dateExpired;
    @Builder.Default
    private String createdBy = "opscloud";
    @Builder.Default
    private Short mfaLevel = 0;
    private String otpSecretKey;

    @Builder.Default
    private String source = "ldap";
    @Builder.Default
    private Date datePasswordLastUpdated = new Date();
    @Builder.Default
    private String comment = "";

}
