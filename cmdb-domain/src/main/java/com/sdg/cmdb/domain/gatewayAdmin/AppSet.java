package com.sdg.cmdb.domain.gatewayAdmin;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class AppSet implements Serializable {
    private static final long serialVersionUID = 8447616260716746368L;

    private String appName;
    private String routePath;

    public AppSet() {
    }

    public AppSet(String appName, String routePath) {
        this.appName = appName;
        this.routePath = routePath;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
