package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class TeamDO implements Serializable {
    private static final long serialVersionUID = -3207846627940612212L;

    private long id;
    private String teamName;
    private String content;
    private int teamType;
    private long teamleaderUserId;
    private String teamleaderUsername;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }


}
