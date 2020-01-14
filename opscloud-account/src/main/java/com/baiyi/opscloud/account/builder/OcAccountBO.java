package com.baiyi.opscloud.account.builder;

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
public class OcAccountBO {

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
    private Date createTime;
    private Date updateTime;
    private String comment;

}
