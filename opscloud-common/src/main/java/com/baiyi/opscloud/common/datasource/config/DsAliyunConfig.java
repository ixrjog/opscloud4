package com.baiyi.opscloud.common.datasource.config;

import com.google.common.base.Joiner;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/6/17 3:51 下午
 * @Version 1.0
 */
public class DsAliyunConfig {

    private static final String RAM_LOGIN_URL = "https://signin.aliyun.com/${COMPANY}.onaliyun.com/login.htm";

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
            if (!StringUtils.isEmpty(this.company)) {
                return Joiner.on("").join("@", company, ".onaliyun.com");
            }
            if (!StringUtils.isEmpty(uid)) {
                return Joiner.on("").join("@", uid, ".onaliyun.com");
            }
            return "";
        }

        /**
         * RAM登录地址
         *
         * @return
         */
        public String getRamLoginUrl() {
            String company = "";
            if (!StringUtils.isEmpty(this.company)) {
                company = this.company;
            } else if (!StringUtils.isEmpty(uid)) {
                company = this.uid;
            }
            return RAM_LOGIN_URL.replace("${COMPANY}", company);
        }
    }
}
