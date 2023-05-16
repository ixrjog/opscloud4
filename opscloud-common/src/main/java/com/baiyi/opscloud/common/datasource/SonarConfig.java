package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/22 1:45 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SonarConfig extends BaseDsConfig {

    private Sonar sonar;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Sonar {

        private String url;
        private String token;

    }

}