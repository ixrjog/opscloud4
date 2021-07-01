package com.baiyi.opscloud.common.base;

/**
 * @Author baiyi
 * @Date 2020/5/6 2:47 下午
 * @Version 1.0
 */
public enum TicketSubscribeType {

    OWN(0), // 本人
    ORG_APPROVAL(1), // ORG审批人
    USERGROUP_APPROVAL(2), // 用户组审批人
    OBSERVER(3); // 观察者

    private int type;

    TicketSubscribeType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
