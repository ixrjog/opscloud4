package com.baiyi.opscloud.dingtalk.builder;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/1/15 10:07 上午
 * @Version 1.0
 */
public class DingtalkTemplateBuilder {

    private DingtalkTemplateMap templateMap = new DingtalkTemplateMap();

    private DingtalkTemplateBuilder() {
    }

    static public DingtalkTemplateBuilder newBuilder() {
        return new DingtalkTemplateBuilder();
    }

    public DingtalkTemplateBuilder paramEntry(String paramName, Object value) {
        templateMap.putContent(paramName, value);
        return this;
    }

    public DingtalkTemplateBuilder paramEntries(Map<String, Object> contents) {
        templateMap.putContents(contents);
        return this;
    }

    public DingtalkTemplateMap build() {
        return templateMap;
    }

}
