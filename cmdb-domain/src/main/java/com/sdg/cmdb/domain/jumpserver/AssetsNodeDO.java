package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class AssetsNodeDO implements Serializable {
    private static final long serialVersionUID = 3168167579587233854L;

    private String id;
    private String key;
    private String value;
    private int child_mark = 0;
    private String date_create;
    private String org_id = "";

    public AssetsNodeDO() {
    }

    public AssetsNodeDO(String id, ServerGroupDO serverGroupDO, String key, String dataCreate) {
        this.id = id;
        this.value = serverGroupDO.getName();
        this.key = key;
        this.date_create = dataCreate;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
