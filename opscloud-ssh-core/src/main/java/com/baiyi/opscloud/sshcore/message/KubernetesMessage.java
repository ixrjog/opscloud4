package com.baiyi.opscloud.sshcore.message;

import com.baiyi.opscloud.domain.model.message.ILoginMessage;
import com.baiyi.opscloud.domain.model.message.ISessionType;
import com.baiyi.opscloud.domain.model.message.IState;
import com.baiyi.opscloud.domain.model.message.ITerminalSize;
import com.baiyi.opscloud.sshcore.model.KubernetesResource;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

/**
 * @Author baiyi
 * @Date 2021/11/16 2:39 下午
 * @Version 1.0
 */
public class KubernetesMessage {

    @EqualsAndHashCode(callSuper = true)
    @Data
    @JsonIgnoreProperties
    public static class Login extends BaseMessage implements ILoginMessage, ISessionType {
        private KubernetesResource data;
        private String sessionType;
        private String token;
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
    public static class Resize extends BaseMessage implements ISessionType, ITerminalSize {
        private KubernetesResource data;
        private String sessionType;
        private String instanceId;

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
    public static class Logout  extends BaseMessage {
        private String instanceId;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    @JsonIgnoreProperties
    public static class Command extends BaseMessage implements ISessionType {
        private String command;
        private String sessionType;
        private String instanceId;
    }

    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @Data
    @JsonIgnoreProperties
    public static class BaseMessage implements IState {
        public static final BaseMessage HEARTBEAT = BaseMessage.builder().build();
        public static final BaseMessage CLOSE =  BaseMessage.builder().build();
        private String id;
        private String state;
        private boolean isAdmin;
        protected com.baiyi.opscloud.sshcore.message.base.BaseMessage.Terminal terminal;
    }

}