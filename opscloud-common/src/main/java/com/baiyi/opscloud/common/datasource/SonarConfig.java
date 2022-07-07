package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/22 1:45 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SonarConfig extends BaseDsConfig {

    private Sonar sonar;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Sonar {

        private String url;
        private String token;

    }

}
