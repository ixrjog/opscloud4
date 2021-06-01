package com.baiyi.caesar.terminal.message;

import com.baiyi.caesar.domain.model.message.IState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author baiyi
 * @Date 2021/5/27 6:20 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BaseMessage implements IState {

    private String id;

    private String state;

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
