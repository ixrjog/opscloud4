package com.baiyi.opscloud.datasource.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/3/23 1:55 下午
 * @Version 1.0
 */
@Data
@Builder
public class Authentication {

    public interface Header {
        String AUTHENTICATION ="Authentication";
    }

    public final static Authentication FREE = Authentication.builder().isFree(true).build();

    @Builder.Default
    private String header = Header.AUTHENTICATION;
    private String token;
    @Builder.Default
    private Boolean isFree = false;
}
