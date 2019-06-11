package com.sdg.cmdb.domain.kubernetes;

import com.alibaba.fastjson.JSON;
import io.fabric8.kubernetes.api.model.Service;
import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesServiceDO implements Serializable {
    private static final long serialVersionUID = 8344529263728673821L;

    private long id;
    private long namespaceId;
    private String name;
    private String appName; // 查询pod需要用到
    private String serviceType;
    private String clusterIP;
    private long serverGroupId;
    private String serverGroupName;
    private String gmtCreate;
    private String gmtModify;

    public KubernetesServiceDO(long namespaceId, Service service) {
        this.namespaceId = namespaceId;
        this.name = service.getMetadata().getName();
        if (service.getSpec().getSelector() != null && service.getSpec().getSelector().containsKey("app"))
            this.appName = service.getSpec().getSelector().get("app");
        this.serviceType = service.getSpec().getType();
        this.clusterIP = service.getSpec().getClusterIP();
    }

    public KubernetesServiceDO(long namespaceId, String name) {
        this.namespaceId = namespaceId;
        this.name = name;
    }

    public KubernetesServiceDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
