package com.baiyi.opscloud.datasource.sonar.param;

import com.google.common.collect.Maps;
import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/10/22 4:37 下午
 * @Version 1.0
 */
@Data
@Builder
public class PagingParam {

    @Builder.Default
    private Integer p = 1;
    @Builder.Default
    private Integer ps = 100;

    public Map<String, String> toParamMap() {
        Map<String, String> params = Maps.newHashMap();
        params.put("p", String.valueOf(p));
        params.put("ps", String.valueOf(ps));
        return params;
    }

}