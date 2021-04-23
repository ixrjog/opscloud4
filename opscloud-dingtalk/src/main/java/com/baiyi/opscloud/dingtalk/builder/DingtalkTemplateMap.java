package com.baiyi.opscloud.dingtalk.builder;

import com.baiyi.opscloud.common.util.JSONUtils;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/1/15 10:07 上午
 * @Version 1.0
 */
@Data
public class DingtalkTemplateMap {

    private Map<String, Object> template = Maps.newHashMap();

    public void putContent(String name, Object value) {
        this.template.put(name, value);
    }

    public void putContents(Map<String, Object> contents) {
        this.template.putAll(contents);
    }

    public Map<String, Object> getContents() {
        return this.template;
    }

    @Override
    public String toString() {
        return JSONUtils.writeValueAsString(this);
    }
}
