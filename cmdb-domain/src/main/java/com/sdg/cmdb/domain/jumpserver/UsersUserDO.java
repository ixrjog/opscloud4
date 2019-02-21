package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户表
 */
@Data
public class UsersUserDO implements Serializable {
    private static final long serialVersionUID = 3135324176176648736L;

    private String id;
    private String password = "";
    private String first_name;
    private String last_name = "";
    private boolean is_active = true;
    private String date_joined;
    private String username;
    private String name;
    private String email;
    private String role = "User";
    private String wechat = "";
    private String phone;
    private String _private_key = "";
    private String _public_key = "";
    private String comment = "";
    private boolean is_first_login = true;
    private String date_expired; // 过期时间
    private String created_by = "opscloud";
    private int otp_level = 0;
    private String source = "ldap";
    private String date_password_last_updated;

    public UsersUserDO(UserDO userDO, String id, String date_joined, String date_expired) {
        this.username = userDO.getUsername();
        this.name = userDO.getDisplayName();
        this.email = userDO.getMail();
        this.phone = userDO.getMobile();
        this.first_name = userDO.getDisplayName();
        this.id = id;
        this.date_joined = date_joined;
        this.date_expired = date_expired;
        this.date_password_last_updated = date_joined;
    }

    public UsersUserDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
