package com.baiyi.opscloud.jumpserver.base;

public class JmsException extends RuntimeException{
    public JmsException() {
    }

    public JmsException(String message) {
        super(message);
    }

    public JmsException(String message, Throwable cause) {
        super(message, cause);
    }

    public JmsException(Throwable cause) {
        super(cause);
    }

    public JmsException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
