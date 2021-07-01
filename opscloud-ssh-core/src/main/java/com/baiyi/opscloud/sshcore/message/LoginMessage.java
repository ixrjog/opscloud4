package com.baiyi.opscloud.sshcore.message;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.sshcore.model.ServerNode;
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
public class LoginMessage extends BaseMessage implements ILoginMessage {

    private Set<ServerNode> serverNodes;

    private String token;


}
