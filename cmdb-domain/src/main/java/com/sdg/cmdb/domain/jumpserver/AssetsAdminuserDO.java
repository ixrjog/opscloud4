package com.sdg.cmdb.domain.jumpserver;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class AssetsAdminuserDO implements Serializable {
    private static final long serialVersionUID = 3768671809780541073L;

    private String id;
    private String name;
    private String username;
    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
