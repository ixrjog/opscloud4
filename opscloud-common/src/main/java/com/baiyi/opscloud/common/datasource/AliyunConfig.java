package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.google.common.base.Joiner;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/6/17 3:54 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AliyunConfig extends BaseConfig {

    private static final String RAM_LOGIN_URL = "https://signin.aliyun.com/${COMPANY}.onaliyun.com/login.htm";

    public static final String DMS_ENDPOINT = "dms-enterprise.aliyuncs.com";

    private Aliyun aliyun;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Aliyun {
        private Account account;
        private String regionId;
        private Set<String> regionIds; // 可用区
        private Ons ons;
        private Dms dms;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
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
        public String getLoginUrl() {
            return RAM_LOGIN_URL.replace("${COMPANY}", StringUtils.isEmpty(this.company) ? this.uid : this.company);
        }
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Ons {
        private String internetRegionId;
    }

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Dms {
        private String endpoint;
        private Long tid;
    }

}
