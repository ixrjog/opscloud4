package com.sdg.cmdb.domain.ci;

import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class CiAppAuthDO implements Serializable {

    private static final long serialVersionUID = 7211399005291019579L;

    private long id;
    private long userId;
    private String username;
    private long appId;
    private String appName;
    private String gmtCreate;
    private String gmtModify;

    public CiAppAuthDO(CiAppDO ciAppDO, UserDO userDO) {
        this.userId = userDO.getId();
        this.username = userDO.getUsername();
        this.appId = ciAppDO.getId();
        this.appName = ciAppDO.getAppName();
    }

    public CiAppAuthDO(long appId, String appName, UserDO userDO) {
        this.userId = userDO.getId();
        this.username = userDO.getUsername();
        this.appId = appId;
        this.appName = appName;
    }

    public CiAppAuthDO() {
    }

}
