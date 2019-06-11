package com.sdg.cmdb.domain.logCleanup;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;
import lombok.Data;

import java.io.Serializable;

@Data
public class LogcleanupVO extends LogcleanupDO implements Serializable {
    private static final long serialVersionUID = 781204267412220111L;

    private ServerDO serverDO;
    private EnvType env;
    private String updateTimeView;
    private String cleanupTimeView;
    private String sizeView;  // 5GB / 98GB

    public void setServerDO(ServerDO serverDO){
        this.serverDO= serverDO;
        this.env = new EnvType(serverDO.getEnvType());
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
