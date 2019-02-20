package com.sdg.cmdb.domain.jumpserver;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权策略 & 用户组 关联表
 */
@Data
public class PermsAssetpermissionUserGroupsDO implements Serializable {
    private static final long serialVersionUID = 5434450772994754554L;

    private int id;
    private String assetpermission_id;
    private String usergroup_id;

    public PermsAssetpermissionUserGroupsDO() {
    }

    public PermsAssetpermissionUserGroupsDO(String assetpermission_id, String usergroup_id) {
        this.assetpermission_id = assetpermission_id;
        this.usergroup_id = usergroup_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
