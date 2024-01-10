package com.baiyi.opscloud.common.builder;

/**
 * @Author baiyi
 * @Date 2021/5/21 4:03 下午
 * @Version 1.0
 */
public class GuacamoleConfigurationBuilder {

    private final SimpleDict guacamoleConfigurationDict = new SimpleDict();

    private GuacamoleConfigurationBuilder() {
    }

    static public GuacamoleConfigurationBuilder newBuilder() {
        return new GuacamoleConfigurationBuilder();
    }

    public GuacamoleConfigurationBuilder putParam(String name, String value) {
        guacamoleConfigurationDict.put(name, value);
        return this;
    }

    public SimpleDict build() {
        return guacamoleConfigurationDict;
    }

}