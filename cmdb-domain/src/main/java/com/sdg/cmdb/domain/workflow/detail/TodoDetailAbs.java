package com.sdg.cmdb.domain.workflow.detail;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;

public abstract class TodoDetailAbs implements Serializable {
    private static final long serialVersionUID = 2780803471467844414L;

<<<<<<< HEAD

    /**
     * 处理状态 0 未处理，1 处理完成  2 ERR
     */
    private int status = 0;

    private String content;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

=======
    private String content;

>>>>>>> develop
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
