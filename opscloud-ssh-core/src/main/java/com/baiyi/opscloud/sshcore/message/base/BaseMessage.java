package com.baiyi.opscloud.sshcore.message.base;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/7/14 5:36 下午
 * @Version 1.0
 */
public class BaseMessage {

    @Data
    @NoArgsConstructor
    public static class Terminal {
        private Integer width;
        private Integer height;
    }

}