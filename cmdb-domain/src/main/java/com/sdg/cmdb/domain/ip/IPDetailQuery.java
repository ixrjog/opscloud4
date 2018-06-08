package com.sdg.cmdb.domain.ip;

import java.io.Serializable;

/**
 * Created by zxxiao on 16/9/28.
 */
public class IPDetailQuery implements Serializable {
    private static final long serialVersionUID = -2170743608979884426L;

    private String ip;

    private long ipNetworkId;

    private int ipType;

    private int useType;

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public long getIpNetworkId() {
        return ipNetworkId;
    }

    public void setIpNetworkId(long ipNetworkId) {
        this.ipNetworkId = ipNetworkId;
    }

    public int getIpType() {
        return ipType;
    }

    public void setIpType(int ipType) {
        this.ipType = ipType;
    }

    public int getUseType() {
        return useType;
    }

    public void setUseType(int useType) {
        this.useType = useType;
    }
}
