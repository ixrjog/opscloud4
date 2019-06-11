package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class TeamuserDO implements Serializable {
    private static final long serialVersionUID = 7220285415834859881L;

    private long id;
    private long teamId;
    private String username;
    private int teamRole;
    private long userId;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
