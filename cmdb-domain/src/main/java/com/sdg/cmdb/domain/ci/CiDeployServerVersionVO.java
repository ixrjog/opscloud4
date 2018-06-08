package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.auth.UserVO;
import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerVO;

import java.io.Serializable;

/**
 * Created by liangjian on 2017/4/13.
 */
public class CiDeployServerVersionVO implements Serializable {
    private static final long serialVersionUID = -6762341868212110679L;

    private long id;

    private long serverId;

    private long ciDeployStatisticsId;

    private ServerVO serverVO;

    private CiDeployStatisticsDO ciDeployStatisticsDO;

    private String gmtCreate;

    private String gmtModify;

    private UserVO userVO;

    public CiDeployServerVersionVO() {
    }

    public CiDeployServerVersionVO(CiDeployServerVersionDO ciDeployServerVersionDO, CiDeployStatisticsDO ciDeployStatisticsDO, ServerVO serverVO) {
        this.serverVO = serverVO;
        this.ciDeployStatisticsDO = ciDeployStatisticsDO;
        if (ciDeployServerVersionDO != null) {
            this.id = ciDeployServerVersionDO.getId();
            this.serverId = ciDeployServerVersionDO.getServerId();
            this.ciDeployStatisticsId = ciDeployServerVersionDO.getId();
            this.gmtCreate = ciDeployServerVersionDO.getGmtCreate();
            this.gmtModify = ciDeployServerVersionDO.getGmtModify();
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getServerId() {
        return serverId;
    }

    public void setServerId(long serverId) {
        this.serverId = serverId;
    }

    public long getCiDeployStatisticsId() {
        return ciDeployStatisticsId;
    }

    public void setCiDeployStatisticsId(long ciDeployStatisticsId) {
        this.ciDeployStatisticsId = ciDeployStatisticsId;
    }

    public CiDeployStatisticsDO getCiDeployStatisticsDO() {
        return ciDeployStatisticsDO;
    }

    public void setCiDeployStatisticsDO(CiDeployStatisticsDO ciDeployStatisticsDO) {
        this.ciDeployStatisticsDO = ciDeployStatisticsDO;
    }

    public ServerVO getServerVO() {
        return serverVO;
    }

    public void setServerVO(ServerVO serverVO) {
        this.serverVO = serverVO;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModify() {
        return gmtModify;
    }

    public void setGmtModify(String gmtModify) {
        this.gmtModify = gmtModify;
    }

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    @Override
    public String toString() {
        return "CiDeployServerVersionVO{" +
                ", id=" + id +
                ", serverId=" + serverId +
                ", ciDeployStatisticsId=" + ciDeployStatisticsId +
                ", gmtCreate='" + gmtCreate + '\'' +
                ", gmtModify='" + gmtModify + '\'' +
                '}';
    }
}
