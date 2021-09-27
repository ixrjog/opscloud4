package com.baiyi.opscloud.ansible.play.message.base;

import com.baiyi.opscloud.domain.model.message.IState;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2021/9/26 5:28 下午
 * @Version 1.0
 */
@Data
@JsonIgnoreProperties
public class BasePlayMessage implements IState {

    private String state;

    private String sessionId;

}

