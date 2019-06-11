package com.sdg.cmdb.domain.kubernetes;

import com.alibaba.fastjson.JSON;
import io.fabric8.kubernetes.api.model.ServicePort;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class KubernetesServicePortDO implements Serializable {
    private static final long serialVersionUID = 8029367151756655798L;

    private long id;
    private long serviceId;
    private String name;
    // nodePort是kubernetes提供给集群外部客户访问service入口的一种方式（另一种方式是LoadBalancer），所以，<nodeIP>:nodePort 是提供给集群外部客户访问service的入口。
    private int nodePort = 0;
    // service暴露在clusterIP上的端口，<cluster ip>:port 是提供给集群内部客户访问service的入口。
    private int port = 0;
    // targetPort很好理解，targetPort是pod上的端口，从port和nodePort上到来的数据最终经过kube-proxy流入到后端pod的targetPort上进入容器。
    private int targetPort = 0;
    private String protocol;
    private String gmtCreate;
    private String gmtModify;

    public KubernetesServicePortDO(long serviceId, ServicePort servicePort) {
        this.serviceId = serviceId;
        if (!StringUtils.isEmpty(servicePort.getName()))
            this.name = servicePort.getName();
        if (servicePort.getNodePort() != null)
            this.nodePort = servicePort.getNodePort();
        if (servicePort.getPort() != null)
            this.port = servicePort.getPort();
        this.protocol = servicePort.getProtocol();
        if (servicePort.getTargetPort() != null)
            this.targetPort = servicePort.getTargetPort().getIntVal();
    }

    public KubernetesServicePortDO() {
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
