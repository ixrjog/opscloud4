package com.sdg.cmdb.domain.task;

import com.alibaba.fastjson.JSON;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DoPlaybook implements Serializable {
    private static final long serialVersionUID = 3089953184684465618L;

    private long taskScriptId;

    /**
     * -e EXTRA_VARS, --extra-vars=EXTRA_VARS
     * set additional variables as key=value or YAML/JSON, if
     * filename prepend with @
     */
    private String extraVars;

    private List<PlaybookHostPattern> playbookServerGroupList;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
