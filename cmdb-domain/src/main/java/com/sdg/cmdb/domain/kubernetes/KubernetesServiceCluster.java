package com.sdg.cmdb.domain.kubernetes;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class KubernetesServiceCluster implements Serializable {

    private static final long serialVersionUID = -2819197392088720974L;

    private List<ServerDO> serverList;
    private int nodePort;
    private KubernetesServiceVO kubernetesServiceVO;


    public KubernetesServiceCluster(List<ServerDO> serverList, KubernetesServiceVO kubernetesServiceVO) {
        this.serverList = serverList;
        this.kubernetesServiceVO = kubernetesServiceVO;
        this.nodePort = kubernetesServiceVO.getNodePort();
    }

    public KubernetesServiceCluster() {
    }

    public int getNodePort() {
        return kubernetesServiceVO.getNodePort();
    }

    public String getPortName() {
        return kubernetesServiceVO.getPortName();
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
