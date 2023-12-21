package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * Aliyun ARMS 数据配置
 *
 * @Author baiyi
 * @Date 2023/6/27 09:42
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunArmsConfig extends BaseDsConfig {

    private Arms arms;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Arms {

        private String version;
        private AliyunConfig.Account account;
        private String regionId;
        @Schema(description = "接入点默认杭州, https://help.aliyun.com/document_detail/441765.html?spm=a2c4g.441908.0.0.8800710dLdFZHW")
        private String endpoint;
        @Schema(description = "控制台链接")
        private Url url;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Url {
        private String home;
    }

}