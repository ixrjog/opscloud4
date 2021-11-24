package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseConfig;
import com.google.common.base.Joiner;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

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

    private Aliyun aliyun;

    @Data
    @NoArgsConstructor
    @ApiModel
    public static class Aliyun {

        private Account account;
        private String regionId;
        private Set<String> regionIds; // 可用区

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
        public String getRamLoginUrl() {
            return RAM_LOGIN_URL.replace("${COMPANY}", StringUtils.isEmpty(this.company) ? this.uid : this.company);
        }

    }

}
