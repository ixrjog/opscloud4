package com.sdg.cmdb.domain.jumpserver;


import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权规则 & 系统账户 关联表
 */
@Data
public class PermsAssetpermissionSystemUsersDO implements Serializable {
    private static final long serialVersionUID = 1497025425785442313L;

    private int id;
    private String assetpermission_id;
    private String systemuser_id;

    public PermsAssetpermissionSystemUsersDO() {
    }

    public PermsAssetpermissionSystemUsersDO(String assetpermission_id, String systemuser_id) {
        this.assetpermission_id = assetpermission_id;
        this.systemuser_id = systemuser_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
