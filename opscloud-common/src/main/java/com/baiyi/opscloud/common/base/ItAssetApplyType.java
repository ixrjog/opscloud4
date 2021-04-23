package com.baiyi.opscloud.common.base;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2020/11/19 5:20 下午
 * @Since 1.0
 */
public enum ItAssetApplyType {

    USE(1, "使用"),
    BORROW(2, "借用"),
    ;

    private int type;
    private String desc;

    ItAssetApplyType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return this.type;
    }
}
