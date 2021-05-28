package com.baiyi.caesar.terminal.message;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/27 6:20 下午
 * @Version 1.0
 */
@Data
public class BaseMessage {

    private String id;
    private String token;
    private String status;

    // 0 普通账户 1 管理员账户
    private Integer loginType;

    private boolean isAdmin;
    private Terminal terminal;

    @Data
    @NoArgsConstructor
    public static class Terminal {
        private Integer width;
        private Integer height;
    }
}
