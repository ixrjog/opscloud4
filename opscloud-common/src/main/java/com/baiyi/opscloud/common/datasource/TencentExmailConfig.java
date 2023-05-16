package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/10/12 3:07 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TencentExmailConfig extends BaseDsConfig {

    private Tencent tencent;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Tencent {
        private Exmail exmail;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Exmail {
        private String apiUrl;
        private String corpId;
        private String name;
        private String corpSecret;
    }

}