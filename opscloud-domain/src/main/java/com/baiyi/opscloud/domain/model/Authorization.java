package com.baiyi.opscloud.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:28 下午
 * @Version 1.0
 */
public class Authorization {

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credential {

        private String password;
        private String username;

        public boolean isEmpty() {
            return (StringUtils.isEmpty(username) || StringUtils.isEmpty(password));
        }

    }
}
