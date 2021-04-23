package com.baiyi.opscloud.zabbix.base;

/**
 * @Author baiyi
 * @Date 2021/2/1 11:17 上午
 * @Version 1.0
 */
public enum  OperatorType {

    /**
     *
     * Condition operator.
     *
     * Possible values:
     * 0 - (default) equals;
     * 1 - does not equal;
     * 2 - contains;
     * 3 - does not contain;
     * 4 - in;
     * 5 - is greater than or equals;
     * 6 - is less than or equals;
     * 7 - not in;
     * 8 - matches;
     * 9 - does not match;
     * 10 - Yes;
     * 11 - No.
     *
     */

    EQUALS(0),
    IS_GREATER_THAN_OR_EQUALS(5)
    ;

    private int type;

    OperatorType(int type) {
        this.type = type;
    }

    public int getType() {
        return this.type;
    }
}
