package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsInstanceConfig;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/1 1:51 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class JenkinsDsInstanceConfig extends BaseDsInstanceConfig {

    private Jenkins jenkins;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Jenkins {

        private String version;
        private String url;
        private String username;
        private String token;

    }

}
