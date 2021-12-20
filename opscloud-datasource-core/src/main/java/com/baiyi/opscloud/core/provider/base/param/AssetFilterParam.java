package com.baiyi.opscloud.core.provider.base.param;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author 修远
 * @Date 2021/6/30 2:29 下午
 * @Since 1.0
 */

@Data
public class AssetFilterParam {

    private Map<String, Object> filter = Maps.newHashMap();

    public void putFilter(String key, Object value) {
        filter.put(key, value);
    }
}
