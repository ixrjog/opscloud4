package com.sdg.cmdb.domain.jumpserver;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 用户 & 用户组 关联表
 */
@Data
public class UsersUserGroupsDO implements Serializable {
    private static final long serialVersionUID = -8622358136197196376L;
    private int id;
    private String user_id;
    private String usergroup_id;

    public UsersUserGroupsDO(String user_id, String usergroup_id) {
        this.user_id = user_id;
        this.usergroup_id = usergroup_id;
    }

    public UsersUserGroupsDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
