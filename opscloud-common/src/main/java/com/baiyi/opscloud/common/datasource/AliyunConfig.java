package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.util.StringFormatter;
import com.google.common.base.Joiner;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Set;

/**
 * Aliyun数据配置
 * @Author baiyi
 * @Date 2021/6/17 3:54 下午
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AliyunConfig extends BaseDsConfig {

    /**
     * signin.aliyun.com
     * private static final String RAM_LOGIN_URL = "https://signin.${VERSION}.com/${COMPANY}.onaliyun.com/login.htm";
     */
    private static final String RAM_LOGIN_URL = "https://signin.{}.com/{}.onaliyun.com/login.htm";

    public static final String DMS_ENDPOINT = "dms-enterprise.aliyuncs.com";

    private Aliyun aliyun;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Aliyun {

        private String version;
        private Account account;
        private String regionId;
        /**
         * 可用区
         */
        private Set<String> regionIds;
        private Ons ons;
        /**
         * 数据库管理
         */
        private Dms dms;
        /**
         * 云监控
         */
        private Cms cms;

        private Acr arc;

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Account {

        private String uid;
        private String name;
        private String company;
        private String accessKeyId;
        private String secret;

        public String getDomain() {
            return Joiner.on("").skipNulls().join("@", StringUtils.isEmpty(this.company) ? this.uid : this.company, ".onaliyun.com");
        }

        /**
         * RAM登录地址
         *
         * @return
         */
        public String getLoginUrl(String version) {
            String aliyunVersion = StringUtils.isEmpty(version) ? "aliyun" : version;
            String aliyunCompany = StringUtils.isEmpty(this.company) ? this.uid : this.company;
            return StringFormatter.arrayFormat(RAM_LOGIN_URL, aliyunVersion, aliyunCompany);
        }

    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Ons {
        private String internetRegionId;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Dms {
        private String endpoint;
        private Long tid;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Cms {
        private Dingtalk dingtalk;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Dingtalk {
        private String token;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Acr {

        private List<Instance> instances;

        @Data
        @NoArgsConstructor
        @Schema
        public static class Instance {
            private String id;
            private String domain;
            private String desc;
        }

    }

}