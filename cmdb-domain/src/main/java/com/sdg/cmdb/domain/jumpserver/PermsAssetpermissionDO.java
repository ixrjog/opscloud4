package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

/**
 * 授权规则
 */
@Data
public class PermsAssetpermissionDO implements Serializable {

    private static final long serialVersionUID = -1848420489524506589L;
    private String id;
    private String name;
    private boolean is_active = true;
    private String date_expired; // 过期时间 2089-01-26 01:17:00.000000
    private String created_by = "opscloud";
    private String date_created; // 创建时间
    private String comment = "";
    private String date_start; // 开始时间
    private String org_id = "";

    public PermsAssetpermissionDO() {
    }

    public PermsAssetpermissionDO(String id, String name, String date_expired, String date_created) {
        this.id = id;
        this.name = name;
        this.date_expired = date_expired;
        this.date_created = date_created;
        this.date_start = date_created;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
