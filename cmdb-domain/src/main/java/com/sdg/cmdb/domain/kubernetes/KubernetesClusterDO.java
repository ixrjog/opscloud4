package com.sdg.cmdb.domain.kubernetes;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesClusterDO implements Serializable {

    private static final long serialVersionUID = -2289384683814089506L;

    private long id;
    private String name;
    private String description;
    private int env;
    private String masterUrl;
    private String kubeconfigFile;
    private String namespace;
    private long serverGroupId;
    private String serverGroupName;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString(){
        return JSON.toJSONString(this);
    }

}
