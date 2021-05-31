package com.baiyi.caesar.terminal.message;

import com.baiyi.caesar.terminal.model.ServerNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2020/5/9 5:07 下午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class LoginMessage extends BaseMessage {

    private Set<ServerNode> serverNodes;

    private String token;


}
