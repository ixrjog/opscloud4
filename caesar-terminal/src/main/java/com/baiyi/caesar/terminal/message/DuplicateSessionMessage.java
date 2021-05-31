package com.baiyi.caesar.terminal.message;

import com.baiyi.caesar.terminal.model.ServerNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Author baiyi
 * @Date 2020/5/13 10:27 上午
 * @Version 1.0
 */
@EqualsAndHashCode(callSuper = true)
@Data
@JsonIgnoreProperties
public class DuplicateSessionMessage extends BaseMessage {

    // 源会话
    private ServerNode duplicateServerNode;
    // 目标会话
    private ServerNode serverNode;

    private String token;


}
