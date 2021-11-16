package com.baiyi.opscloud.sshcore.message.server;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.sshcore.model.ServerNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/16 11:24 上午
 * @Version 1.0
 */
public class ServerMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class BatchCommand extends BaseServerMessage {
        private Boolean isBatch; // 会话批量指令
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Close extends BaseServerMessage {
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Command extends BaseServerMessage {
        private String instanceId;
        private String data;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class DuplicateSession extends BaseServerMessage {
        // 源会话
        private ServerNode duplicateServerNode;
        // 目标会话
        private ServerNode serverNode;
        private String token;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Login extends BaseServerMessage implements ILoginMessage {
        private Set<ServerNode> serverNodes;
        private String token;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Logout extends BaseServerMessage {
        private String instanceId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Resize extends BaseServerMessage {
        private String instanceId;
    }
}
