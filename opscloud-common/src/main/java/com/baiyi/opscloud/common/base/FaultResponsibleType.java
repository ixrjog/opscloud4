package com.baiyi.opscloud.common.base;

import lombok.Getter;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/1/6 5:57 下午
 * @Since 1.0
 */

@Getter
public enum FaultResponsibleType {

    primaryResponsiblePerson(1, "主要责任人"),
    secondaryResponsiblePerson(2, "次要责任人"),
    ;

    private int type;
    private String desc;

    FaultResponsibleType(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }

    public int getType() {
        return this.type;
    }
}
