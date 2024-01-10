package com.baiyi.opscloud.common.datasource.base;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/10/9 13:32
 * @Version 1.0
 */
public class DsManageConfig {

    @Data
    @NoArgsConstructor
    @Schema
    public static class Manage {

        @Schema(description = "terminal")
        private String type;
        private Server server;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Server {

        @Schema(description = "Server ID")
        private Integer id;
        private String name;

    }

}