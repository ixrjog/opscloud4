package com.baiyi.opscloud.common.builder;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/5/18 2:41 下午
 * @Version 1.0
 */
@Data
public class SimpleDict {

    private Map<String, String> dict = Maps.newHashMap();

    public void put(String name, String value) {
        this.dict.put(name, value);
    }

    public void put(Map<String, String> variable) {
        this.dict.putAll(variable);
    }

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }
}
