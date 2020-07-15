package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/7/11 3:23 下午
 * @Version 1.0
 */
public enum ServerTaskType {

    COMMAND(0),
    SCRIPT(1),
    PLAYBOOK(2);

    private int type;

    ServerTaskType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
