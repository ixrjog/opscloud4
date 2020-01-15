package com.baiyi.opscloud.account.builder;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @Author baiyi
 * @Date 2020/1/15 9:22 上午
 * @Version 1.0
 */
@Builder
@Data
public class OcUserBO {

    private Integer id;
    private String username;
    private String password;
    private String name;
    private String displayName;
    private String email;
    @Builder.Default
    private Boolean isActive = true;
    private Integer lastLogin;
    private String wechat;
    private String phone;
    private String createdBy;
    private String source;
    private Date createTime;
    private Date updateTime;
    private String comment;

}
