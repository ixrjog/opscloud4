package com.baiyi.opscloud.domain.model;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;

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

        public String toBasic() {
            String authString = Joiner.on(":").join(username, password);
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8));
            return new String(authEncBytes);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {

        private String token;

        public boolean isEmpty() {
            return (StringUtils.isEmpty(token));
        }

        public String toBasic() {
            String authString = token + ":";
            byte[] authEncBytes = Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8));
            return new String(authEncBytes);
        }

    }
}
