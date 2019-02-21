package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产关联表
 */
@Data
public class AssetsAssetNodesDO implements Serializable {
    private static final long serialVersionUID = -8855912848428191226L;

    private String asset_id;
    private String node_id;

    public AssetsAssetNodesDO() {
    }

    public AssetsAssetNodesDO(String asset_id, String node_id) {
        this.asset_id = asset_id;
        this.node_id = node_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
