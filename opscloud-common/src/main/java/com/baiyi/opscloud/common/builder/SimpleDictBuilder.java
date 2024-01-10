package com.baiyi.opscloud.common.builder;

import org.apache.commons.lang3.StringUtils;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/16 1:24 下午
 * @Version 1.0
 */
public class SimpleDictBuilder {

    private final SimpleDict simpleDict = new SimpleDict();

    private SimpleDictBuilder() {
    }

    static public SimpleDictBuilder newBuilder() {
        return new SimpleDictBuilder();
    }

    public SimpleDictBuilder put(String name, String value) {
        if (!StringUtils.isEmpty(value)) {
            simpleDict.put(name, value);
        }
        return this;
    }

    public SimpleDictBuilder put(Map<String, String> dict) {
        if (dict != null) {
            simpleDict.put(dict);
        }
        return this;
    }

    public SimpleDict build() {
        return simpleDict;
    }

}