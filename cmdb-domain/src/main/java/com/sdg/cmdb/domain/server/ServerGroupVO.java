package com.sdg.cmdb.domain.server;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.config.ConfigPropertyGroupDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by zxxiao on 2016/10/31.
 */
@Data
public class ServerGroupVO implements Serializable {
    private static final long serialVersionUID = -6802315117466446186L;

    private long id;

    private String name;

    private String content;

    private String ipNetwork;

    private int useType;

    private String gmtCreate;

    private String gmtModify;

    private long serverCnt;

    private int keyboxCnt;

    private int userCnt;

    private ServerGroupUseTypeDO serverGroupUseTypeDO;

    private List<String> users;

    private List<ConfigPropertyGroupDO> groupDOList;

    public ServerGroupVO() {
    }

    public ServerGroupVO(ServerGroupDO groupDO, List<ConfigPropertyGroupDO> groupDOList) {
        this.id = groupDO.getId();
        this.name = groupDO.getName();
        this.content = groupDO.getContent();
        this.ipNetwork = groupDO.getIpNetwork();
        this.useType = groupDO.getUseType();
        this.gmtCreate = groupDO.getGmtCreate();
        this.gmtModify = groupDO.getGmtModify();
        this.groupDOList = groupDOList;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
