package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2023/5/12 10:23
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunDevopsConfig extends BaseDsConfig {

    private AliyunDevopsConfig.Devops devops;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Devops {

        private String version;
        private AliyunConfig.Account account;
        @Schema(description = "组织架构ID")
        private String organizationId;
        private String regionId;

    }

}
