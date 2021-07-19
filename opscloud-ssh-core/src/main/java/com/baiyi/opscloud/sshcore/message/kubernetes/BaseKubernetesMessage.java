package com.baiyi.opscloud.sshcore.message.kubernetes;

import com.baiyi.opscloud.domain.model.message.IState;
import com.baiyi.opscloud.sshcore.message.base.BaseMessage;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/7/14 5:32 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BaseKubernetesMessage implements IState {

    public static BaseKubernetesMessage HEARTBEAT = new BaseKubernetesMessage();

    public static BaseKubernetesMessage CLOSE = new BaseKubernetesMessage();

    private String id;

    private String state;

    private boolean isAdmin;

    protected BaseMessage.Terminal terminal;

}

