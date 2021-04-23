package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/12/10 11:35 上午
 * @Since 1.0
 */

@Getter
public enum AliyunSLBSCType {

    DEFAULT(1, "默认证书"),
    EXTENSION(2, "拓展证书");

    private int code;
    private String msg;

    AliyunSLBSCType(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}
