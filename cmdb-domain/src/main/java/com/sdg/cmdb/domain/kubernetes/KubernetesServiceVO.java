package com.sdg.cmdb.domain.kubernetes;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KubernetesServiceVO extends KubernetesServiceDO implements Serializable {
    private static final long serialVersionUID = 6360045309139588779L;

    private KubernetesNamespaceVO namespace;

    private String portName;
    private String podIp;
    private int nodePort;
    private List<KubernetesServicePortDO> servicePortList;

}
