package com.baiyi.opscloud.otp.model;

import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2022/3/9 3:29 PM
 * @Version 1.0
 */
public class OTPAccessCode {

    @Data
    @Builder
    public static class AccessCode {
        // 第一组密码
        private String currentPassword;
        // 第二组密码
        private String futurePassword;
    }

}