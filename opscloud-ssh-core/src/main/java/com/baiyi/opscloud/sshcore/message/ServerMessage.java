package com.baiyi.opscloud.sshcore.message;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.model.message.IState;
import com.baiyi.opscloud.domain.model.message.ITerminalSize;
import com.baiyi.opscloud.sshcore.model.ServerNode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2021/11/16 11:24 上午
 * @Version 1.0
 */
public class ServerMessage {

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties
    public static class BaseMessage implements IState, ITerminalSize {

        public static final BaseMessage CLOSE = BaseMessage.builder().build();

        private String id;
        private String state;
        // 0 普通账户 1 管理员账户
        private Integer loginType;
        private boolean isAdmin;
        private com.baiyi.opscloud.sshcore.message.base.BaseMessage.Terminal terminal;

        @Override
        public int getWidth() {
            return this.terminal.getWidth();
        }

        @Override
        public int getHeight() {
            return this.terminal.getHeight();
        }
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class BatchCommand extends BaseMessage {
        private Boolean isBatch; // 会话批量指令
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Command extends BaseMessage {
        private String instanceId;
        private String data;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class DuplicateSession extends BaseMessage {
        // 源会话
        private ServerNode duplicateServerNode;
        // 目标会话
        private ServerNode serverNode;
        private String token;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Login extends BaseMessage implements ILoginMessage {
        private Set<ServerNode> serverNodes;
        private String token;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Logout extends BaseMessage {
        private String instanceId;
    }

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Resize extends BaseMessage {
        private String instanceId;
    }

}