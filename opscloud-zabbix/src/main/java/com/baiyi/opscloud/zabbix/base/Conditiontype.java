package com.baiyi.opscloud.zabbix.base;

/**
 * @Author baiyi
 * @Date 2021/2/1 11:22 上午
 * @Version 1.0
 */
public enum Conditiontype {
    /**
     * Type of condition.
     * <p>
     * Possible values for trigger actions:
     * 0 - host group;
     * 1 - host;
     * 2 - trigger;
     * 3 - trigger name;
     * 4 - trigger severity;
     * 6 - time period;
     * 13 - host template;
     * 15 - application;
     * 16 - problem is suppressed;
     * 25 - event tag;
     * 26 - event tag value.
     * <p>
     * Possible values for discovery actions:
     * 7 - host IP;
     * 8 - discovered service type;
     * 9 - discovered service port;
     * 10 - discovery status;
     * 11 - uptime or downtime duration;
     * 12 - received value;
     * 18 - discovery rule;
     * 19 - discovery check;
     * 20 - proxy;
     * 21 - discovery object.
     * <p>
     * Possible values for autoregistration actions:
     * 20 - proxy;
     * 22 - host name;
     * 24 - host metadata.
     * <p>
     * Possible values for internal actions:
     * 0 - host group;
     * 1 - host;
     * 13 - host template;
     * 15 - application;
     * 23 - event type.
     */
    HOST_GROUP(0),
    // trigger severity
    TRIGGER_SEVERITY(4),
    // event tag value
    EVENT_TAG_VALUE(26);

    private int type;

    Conditiontype(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
