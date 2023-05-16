package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/4 6:11 PM
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GuacamoleConfig extends BaseDsConfig {

    private static final int DEF_PORT = 4822;

    private Guacamole guacamole;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Guacamole {

        private String host;
        private Integer port;
        private List<String> protocolSupport;

        public Integer getPort() {
            if (this.port == null || this.port <= 0) {
                return DEF_PORT;
            }
            return this.port;
        }
    }

}