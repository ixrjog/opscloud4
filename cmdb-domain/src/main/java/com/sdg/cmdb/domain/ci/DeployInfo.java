package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.serializer.SerializerFeature;
import lombok.Data;

import java.io.Serializable;

@Data
public class DeployInfo implements Serializable {

    private static final long serialVersionUID = -3471509451958489467L;
    private String appName;
    private JSONArray versionData;

    public DeployInfo() {
    }

    public DeployInfo(String appName, JSONArray versionData) {
        this.appName = appName;
        this.versionData = versionData;
    }

    @Override
    public String toString(){
        return JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect);
    }
}
