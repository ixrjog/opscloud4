package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;

/**
 * 资产-主机
 */
@Data
public class AssetsAssetDO implements Serializable {
    private static final long serialVersionUID = -5863460314844140066L;

    private String id;
    private String ip;
    private String hostname;
    private int port = 22;
    private boolean is_active = true;
    // is_active
    private String platform = "Linux"; // Linux
    private String admin_user_id;  // assets_adminuser:id  账户

    private String created_by ="opscloud";
    private String comment = "opscloud server";
    private String protocol = "ssh";
    private String org_id = "";

    public AssetsAssetDO() {
    }

    public AssetsAssetDO(String id, ServerDO serverDO, String admin_user_id) {
        this.id = id;
        this.ip = serverDO.getInsideIp();
        this.hostname = serverDO.acqServerName();
        this.admin_user_id = admin_user_id;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
