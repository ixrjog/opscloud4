package com.baiyi.opscloud.account.bo;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:26 上午
 * @Version 1.0
 */
@Data
@Builder
public class AccountBO {

    private Integer id;
    private String accountUid;
    private String accountId;
    private Integer accountType;
    private Integer userId;
    private String username;
    private String password;
    private String name;
    private String displayName;
    private String email;
    private Boolean isActive;
    private Integer lastLogin;
    private String wechat;
    private String phone;
    @Builder.Default
    private Integer sshKey = 0;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
