package com.sdg.cmdb.domain.logCleanup;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.server.EnvType;
import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/3/30.
 */
@Data
public class LogcleanupDO implements Serializable {
    private static final long serialVersionUID = 5950320978859804002L;

    public static final String DISKTYPE_SYS = "SYS";
    public static final String DISKTYPE_DATA = "DATA";

    public static final int HISTORY_DEFAULT = 30;

    private long id;
    private long serverGroupId;
    private String groupName;
    private float history = HISTORY_DEFAULT;            //归档天数
    private int envType;
    private boolean enabled = false;          //是否启用
    private long scriptId;
    private String scriptParams;      //脚本参数（可选）
    private String updateTime;        //采样更新时间 只取pfree
    private String cleanupTime;       //清理时间
    private boolean cleanupResult = false;    //清理结果
    private long serverId;
    private String serverName;
    private String serialNumber;
    private String diskType;          //磁盘类型 SYS / DATA
    private int freeDiskSpace;        //磁盘使用率
    private long usedDiskSpace;       //使用容量  转GB / 1024 * 1024 * 1024
    private long totalDiskSpace;      //总容量    转GB / 1024 * 1024 * 1024

    private String gmtCreate;
    private String gmtModify;

    public LogcleanupDO() {
    }

    public LogcleanupDO(ServerDO serverDO) {
        this.serverGroupId = serverDO.getServerGroupId();
        this.serverId = serverDO.getId();
        this.serverName = serverDO.getServerName();
        this.envType = serverDO.getEnvType();
        this.serialNumber = serverDO.getSerialNumber();
    }

    public String acqServerName() {
        if (this.envType == EnvType.EnvTypeEnum.prod.getCode()) {
            return serverName + "-" + serialNumber;
        } else {
            return serverName + "-" + EnvType.EnvTypeEnum.getEnvTypeName(envType) + "-" + serialNumber;
        }
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
