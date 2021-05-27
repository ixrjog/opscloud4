package com.baiyi.caesar.common.base;

/**
 * @Author baiyi
 * @Date 2021/1/26 3:59 下午
 * @Version 1.0
 */
public enum BlockRuleLevel {

    P0("P0"),
    P1("P1"),
    P2("P2"),
    P3("P3"),
    P4("P4");

    private String level;

    BlockRuleLevel(String level) {
        this.level = level;
    }

    public String getLevel() {
        return this.level;
    }
}
