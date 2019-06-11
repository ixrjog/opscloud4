package com.sdg.cmdb.domain.kubernetes;

import lombok.Data;

import java.io.Serializable;

@Data
public class KubernetesNamespaceVO extends KubernetesNamespaceDO implements Serializable {

    private static final long serialVersionUID = 3753718439683707613L;

    private String clusterName;

}
