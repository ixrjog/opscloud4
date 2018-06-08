package com.sdg.cmdb.domain.dingtalk;

import java.io.Serializable;

/**
 * Created by zxxiao on 2017/6/29.
 */
public class DingTalkContent implements Serializable {
    private static final long serialVersionUID = -8667155647365249123L;

    /**
     * 通知地址
     */
    private String webHook;

    private String msg;

    public String getWebHook() {
        return webHook;
    }

    public void setWebHook(String webHook) {
        this.webHook = webHook;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
