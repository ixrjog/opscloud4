package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权策略 & 节点（概念等同服务器组） 关联表
 */
@Data
public class PermsAssetpermissionNodesDO implements Serializable {
    private static final long serialVersionUID = 8633812503316112779L;

    private int id;
    private String assetpermission_id;
    private String node_id;

    public PermsAssetpermissionNodesDO() {
    }

    public PermsAssetpermissionNodesDO(String assetpermission_id, String node_id) {
        this.assetpermission_id = assetpermission_id;
        this.node_id = node_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
