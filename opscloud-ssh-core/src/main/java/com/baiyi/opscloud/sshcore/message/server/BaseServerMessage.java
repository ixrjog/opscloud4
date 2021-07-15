package com.baiyi.opscloud.sshcore.message.server;

import com.baiyi.opscloud.domain.model.message.IState;
import com.baiyi.opscloud.sshcore.message.base.BaseMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/5/27 6:20 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BaseServerMessage implements IState {

    private String id;

    private String state;

    // 0 普通账户 1 管理员账户
    private Integer loginType;

    private boolean isAdmin;

    private BaseMessage.Terminal terminal;

}
