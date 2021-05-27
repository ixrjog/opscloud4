package com.baiyi.caesar.common.builder;

/**
 * @Author baiyi
 * @Date 2021/5/21 4:03 下午
 * @Version 1.0
 */
public class GuacamoleConfigurationBuilder {

    private CommonDict guacamoleConfigurationDict = new CommonDict();

    private GuacamoleConfigurationBuilder() {
    }

    static public GuacamoleConfigurationBuilder newBuilder() {
        return new GuacamoleConfigurationBuilder();
    }

    public GuacamoleConfigurationBuilder paramEntry(String name, String value) {
        guacamoleConfigurationDict.put(name, value);
        return this;
    }

    public CommonDict build() {
        return guacamoleConfigurationDict;
    }
}
