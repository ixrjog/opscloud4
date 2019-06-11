package com.sdg.cmdb.domain.workflow;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowGroupDO implements Serializable {

    private static final long serialVersionUID = 3300371209668140766L;

    private long id;
    private String groupName;
    private String content;
    /**
     * 组类型废弃，主要使用 WorkflowDO.WfTypeEnum
     */
    private int groupType;
    private String gmtCreate;
    private String gmtModify;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }
}
