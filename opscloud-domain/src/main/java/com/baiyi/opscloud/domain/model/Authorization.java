package com.baiyi.opscloud.domain.model;

import com.google.common.base.Joiner;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;

/**
 * @Author baiyi
 * @Date 2021/5/18 4:28 下午
 * @Version 1.0
 */
public class Authorization {

    public interface IToBasic {

        String toBasic();

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Credential implements IToBasic {

        private String password;
        private String username;

        public boolean isEmpty() {
            return (StringUtils.isEmpty(username) || StringUtils.isEmpty(password));
        }

        public String toBasic() {
            String authString = Joiner.on(":").join(username, password);
            return Authorization.toBasic(authString);
        }

    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token implements IToBasic {

        private String token;

        public boolean isEmpty() {
            return (StringUtils.isEmpty(token));
        }

        public String toBasic() {
            String authString = token + ":";
            return Authorization.toBasic(authString);
        }

    }

    private static String toBasic(String authString){
        byte[] authEncBytes = Base64.encodeBase64(authString.getBytes(StandardCharsets.UTF_8),false);
        return new String(authEncBytes);
    }

}