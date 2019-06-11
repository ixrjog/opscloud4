package com.sdg.cmdb.domain.kubernetes;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesNamespaceDO implements Serializable {
    private static final long serialVersionUID = 5036629722741224715L;

    private long id;
    private long clusterId;
    private String namespace;
    private String gmtCreate;
    private String gmtModify;

    public KubernetesNamespaceDO(long clusterId, String namespace) {
        this.clusterId = clusterId;
        this.namespace = namespace;
    }

    public KubernetesNamespaceDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
