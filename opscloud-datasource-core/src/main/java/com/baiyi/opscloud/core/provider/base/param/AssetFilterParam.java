package com.baiyi.opscloud.core.provider.base.param;

import com.google.common.collect.Maps;
import lombok.Data;

import java.util.Map;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
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
