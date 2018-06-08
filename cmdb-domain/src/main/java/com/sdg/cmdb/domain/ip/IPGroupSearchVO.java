package com.sdg.cmdb.domain.ip;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/9.
 */
public class IPGroupSearchVO implements Serializable {
    private static final long serialVersionUID = 5540879268327991245L;

    private String ipNetwork;

    private long serverGroupId;

    private int ipType;

    public String getIpNetwork() {
        return ipNetwork;
    }

    public void setIpNetwork(String ipNetwork) {
        this.ipNetwork = ipNetwork;
    }

    public long getServerGroupId() {
        return serverGroupId;
    }

    public void setServerGroupId(long serverGroupId) {
        this.serverGroupId = serverGroupId;
    }

    public int getIpType() {
        return ipType;
    }

    public void setIpType(int ipType) {
        this.ipType = ipType;
    }

    @Override
    public String toString() {
        return "IPGroupSearchVO{" +
                "ipNetwork='" + ipNetwork + '\'' +
                ", serverGroupId=" + serverGroupId +
                ", ipType=" + ipType +
                '}';
    }
}
