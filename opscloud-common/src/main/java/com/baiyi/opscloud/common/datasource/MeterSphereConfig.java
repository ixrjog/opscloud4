package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * MeterSphere数据配置
 *
 * @Author baiyi
 * @Date 2023/5/15 11:18
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class MeterSphereConfig extends BaseDsConfig {

    private MeterSphere meterSphere;

    @Data
    @NoArgsConstructor
    @Schema
    public static class MeterSphere {
        private Hook hook;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Hook {
        private String buildUrl;
        private String deployUrl;
    }

}