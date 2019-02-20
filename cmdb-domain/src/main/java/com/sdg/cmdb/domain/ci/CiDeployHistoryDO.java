package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;

@Data
public class CiDeployHistoryDO implements Serializable {

    private static final long serialVersionUID = -3720824615779998122L;

    private long id;
    private long buildId;
    private long serverId;
    private String groupName;
    // TODO 保留但不使用
    private String username;
    private String displayName;
    private String branch;
    // TODO 保留但不使用
    private String tag;
    private String projectName;
    private String buildStatus;
    private String buildPhase;
    private String versionName;
    private String versionDesc;
    private int buildNumber;
    private boolean rollback = false;
    private int zabbixStatus;
    private String serialNumber;
    private String serverName;
    private String insideIp;

    public CiDeployHistoryDO(ServerDO server, CiJobVO ciJob, CiBuildVO ciBuild) {
        this.serverId = server.getId();
        this.serialNumber = server.getSerialNumber();
        this.serverName = server.getServerName();
        this.insideIp = server.getInsideIp();
        this.buildId = ciBuild.getId();
        this.groupName = ciJob.getCiAppVO().getServerGroupName();
        this.displayName = ciBuild.getDisplayName();
        this.branch = ciJob.getBranch();
        this.projectName = ciJob.getCiAppVO().getProjectName();
        //     private String buildStatus;
        //     private String buildPhase;
        this.versionName = ciBuild.getVersionName();
        this.versionDesc = ciBuild.getVersionDesc();
        this.buildNumber = ciBuild.getBuildNumber();
    }

    public CiDeployHistoryDO() {
    }


    /**
     * Dingtalk消息通知
     *
     * @return
     */
    public String getDeployVersionInfo() {
        if (StringUtils.isEmpty(this.versionName)) {
            return "分支:" + branch + "/" + "编号:" + buildNumber;
        } else {
            return versionName + " 编号:" + buildNumber;
        }

    }

}
