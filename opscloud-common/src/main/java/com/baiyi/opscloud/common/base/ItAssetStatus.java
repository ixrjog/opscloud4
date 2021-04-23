package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 4:12 下午
 * @Since 1.0
 */

@Getter
public enum ItAssetStatus {
    FREE(1, "空闲"),
    USED(2, "在用"),
    BORROW(3, "借用"),
    DISPOSE(4, "处置"),
    ;

    private int type;
    private String desc;

    ItAssetStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return this.type;
    }

}
