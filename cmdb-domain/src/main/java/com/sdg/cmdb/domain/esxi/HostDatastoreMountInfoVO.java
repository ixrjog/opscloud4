package com.sdg.cmdb.domain.esxi;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/8/2.
 */
public class HostDatastoreMountInfoVO implements Serializable {
    private static final long serialVersionUID = -326468120912337106L;

    private String path;
    private String accessMode;//访问模式
    private Boolean mounted;  //挂载状态
    private Boolean accessible;  //数据存储是目前从主机访问

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
    public String getAccessMode() {
        return accessMode;
    }
    public void setAccessMode(String accessMode) {
        this.accessMode = accessMode;
    }
    public Boolean getMounted() {
        return mounted;
    }
    public void setMounted(Boolean mounted) {
        this.mounted = mounted;
    }
    public Boolean getAccessible() {
        return accessible;
    }
    public void setAccessible(Boolean accessible) {
        this.accessible = accessible;
    }
}
