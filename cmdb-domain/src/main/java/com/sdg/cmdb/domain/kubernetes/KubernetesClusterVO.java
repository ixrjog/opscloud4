package com.sdg.cmdb.domain.kubernetes;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KubernetesClusterVO extends KubernetesClusterDO implements Serializable {
    private static final long serialVersionUID = 1819583111800786888L;

    private List<KubernetesNamespaceDO> namespaceList;


}
