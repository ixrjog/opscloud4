package com.sdg.cmdb.domain.keybox;

/**
 * Created by zxxiao on 2017/2/20.
 */
public enum WSContentType {
    response(0, "正常响应"),
    loginSuccss(1, "登录成功"),
    loginFailure(2, "登录失败");
    private int code;
    private String name;

    WSContentType(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}