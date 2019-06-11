package com.sdg.cmdb.domain.nginx;


import com.sdg.cmdb.domain.kubernetes.KubernetesServiceVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class NginxTcpDubboDO implements Serializable {
    private static final long serialVersionUID = 854150475272969859L;

    private long id;
    private String clusterKey;
    private long kubernetesServiceId;
    private String dubbo;
    private String podIp;
    private int servicePort;

    private String gmtCreate;
    private String gmtModify;

    public NginxTcpDubboDO(String clusterKey, KubernetesServiceVO serviceVO, String dubbo) {
        this.clusterKey = clusterKey;
        this.kubernetesServiceId = serviceVO.getId();
        this.podIp = serviceVO.getPodIp();
        this.servicePort = serviceVO.getNodePort();
        this.dubbo = dubbo;
    }

    public NginxTcpDubboDO() {
    }

}
