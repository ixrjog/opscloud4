package com.baiyi.opscloud.domain.vo.account;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/12/9 11:36 上午
 * @Version 1.0
 */
public class AccountVO {

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Account implements Serializable {

        private static final long serialVersionUID = -4803879426888379993L;

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

        private Integer sshKey;

        private Date createTime;

        private Date updateTime;

        private String comment;
    }
}
