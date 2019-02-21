package com.sdg.cmdb.domain.ci;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.List;

public class JobParams implements Serializable {
    private static final long serialVersionUID = -7645704576542212578L;

    public JobParams() {
    }

    public JobParams(List<JobParam> params) {
        this.params = params;
    }

    private List<JobParam> params;


    public List<JobParam> getParams() {
        return params;
    }

    public void setParams(List<JobParam> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
