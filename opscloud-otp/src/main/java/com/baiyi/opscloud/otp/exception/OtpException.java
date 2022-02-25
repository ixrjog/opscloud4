package com.baiyi.opscloud.otp.exception;

/**
 * @Author baiyi
 * @Date 2022/2/25 2:27 PM
 * @Version 1.0
 */
public class OtpException {

    public static class DecodingException extends Exception {
        public DecodingException(String message) {
            super(message);
        }
    }

}
