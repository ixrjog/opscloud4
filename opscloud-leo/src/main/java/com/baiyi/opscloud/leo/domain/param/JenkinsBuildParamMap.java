package com.baiyi.opscloud.leo.domain.param;

import com.baiyi.opscloud.common.util.JSONUtil;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2022/11/11 17:40
 * @Version 1.0
 */
public class JenkinsBuildParamMap {

    private final Map<String, String> params = Maps.newHashMap();

    public void putParam(String key, String value) {
        this.params.put(key, value);
    }

    public void putParams(Map<String, String> params) {
        this.params.putAll(params);
    }

    @Override
    public String toString() {
        return JSONUtil.writeValueAsString(this);
    }

}