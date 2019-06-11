package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@Data
public class EcsTaskVO implements Serializable {
    private static final long serialVersionUID = -6181744747263649625L;

    private long id;
    private int cnt; // 任务总数量
    private int serverCnt;
    private boolean complete;
    private UserDO userDO;
    private List<EcsServerDO> serverList;


    public EcsTaskVO() {
    }

    public EcsTaskVO(EcsTaskDO ecsTaskDO, List<EcsServerDO> serverList) {
        this.id = ecsTaskDO.getId();
        this.cnt = ecsTaskDO.getCnt();
        this.complete = ecsTaskDO.isComplete();
        this.serverCnt = serverList.size();
        this.serverList = serverList;
    }

}
