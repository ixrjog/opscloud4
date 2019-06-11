package com.sdg.cmdb.domain.server;

import com.sdg.cmdb.domain.auth.UserDO;
import lombok.Data;

import java.io.Serializable;

@Data
public class EcsTaskDO implements Serializable {
    private static final long serialVersionUID = -1523756426624375465L;

    private long id;
    private String taskName = "CreateECS";
    private int cnt;
    private boolean complete = false;
    private long userId;
    private String gmtModify;
    private String gmtCreate;

    public EcsTaskDO() {
    }

    public EcsTaskDO(UserDO userDO, int cnt) {
        this.userId = userDO.getId();
        this.cnt = cnt;
    }

}
