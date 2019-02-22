package com.sdg.cmdb.domain.workflow.detail;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public abstract class TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 2780803471467844414L;

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
