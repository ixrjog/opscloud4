package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author 修远
 * @Date 2022/8/31 6:13 PM
 * @Since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LXHLConfig extends BaseDsConfig {

    private Account account;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Account {

        private String url;
        private String username;
        private String password;

    }

}