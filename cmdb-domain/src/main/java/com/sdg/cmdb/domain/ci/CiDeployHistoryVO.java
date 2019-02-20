package com.sdg.cmdb.domain.ci;


import com.sdg.cmdb.domain.server.ServerDO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper=false)
public class CiDeployHistoryVO extends CiDeployHistoryDO implements Serializable, Comparable<CiDeployHistoryVO> {


    private CiBuildDO ciBuildDO;

    public CiDeployHistoryVO() {

    }


    public CiDeployHistoryVO(ServerDO serverDO) {
        setServerId(serverDO.getId());
        setSerialNumber(serverDO.getSerialNumber());
        setInsideIp(serverDO.getInsideIp());
        setServerName(serverDO.getServerName());
    }


    public CiDeployHistoryVO(CiDeployHistoryDO ciDeployHistoryDO, CiBuildDO ciBuildDO) {
        setId(ciDeployHistoryDO.getId());
        setBuildId(ciDeployHistoryDO.getBuildId());
        setServerId(ciDeployHistoryDO.getServerId());
        setGroupName(ciDeployHistoryDO.getGroupName());
        setUsername(ciDeployHistoryDO.getUsername());
        setDisplayName(ciDeployHistoryDO.getDisplayName());
        setBranch(ciDeployHistoryDO.getBranch());
        setProjectName(ciDeployHistoryDO.getProjectName());
        setBuildStatus(ciDeployHistoryDO.getBuildStatus());
        setBuildPhase(ciDeployHistoryDO.getBuildPhase());
        setVersionName(ciDeployHistoryDO.getVersionName());
        setVersionDesc(ciDeployHistoryDO.getVersionDesc());
        setBuildNumber(ciDeployHistoryDO.getBuildNumber());
        setRollback(ciDeployHistoryDO.isRollback());
        setZabbixStatus(ciDeployHistoryDO.getZabbixStatus());
        setSerialNumber(ciDeployHistoryDO.getSerialNumber());
        setInsideIp(ciDeployHistoryDO.getInsideIp());
        setServerName(ciDeployHistoryDO.getServerName());
        this.ciBuildDO = ciBuildDO;
    }

    @Override
    public int compareTo(CiDeployHistoryVO ciDeployHistoryVO) {
        //自定义比较方法，如果认为此实体本身大则返回1，否则返回-1
        try {
            if (Integer.valueOf(getSerialNumber()) >= Integer.valueOf(ciDeployHistoryVO.getSerialNumber()))
                return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

}
