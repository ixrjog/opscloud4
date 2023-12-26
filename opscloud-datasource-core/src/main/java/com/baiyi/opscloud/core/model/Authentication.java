package com.baiyi.opscloud.core.model;

import com.google.common.base.Joiner;
import lombok.Builder;
import lombok.Data;

import static com.baiyi.opscloud.core.model.Authentication.Header.AUTHENTICATION;

/**
 * @Author baiyi
 * @Date 2021/3/23 1:55 下午
 * @Version 1.0
 */
@Data
@Builder
public class Authentication {

    public interface Header {
        String AUTHENTICATION = "Authentication";
    }

    public static final Authentication FREE = Authentication.builder().isFree(true).build();

    @Builder.Default
    private String header = AUTHENTICATION;
    private String token;
    @Builder.Default
    private Boolean isFree = false;

    public String toAuthentication() {
        return Joiner.on(" ").join(AUTHENTICATION, this.token);
    }

}