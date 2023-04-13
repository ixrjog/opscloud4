package com.baiyi.opscloud.sshcore.config;

import com.google.common.base.Joiner;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/5/25 1:45 下午
 * @Version 1.0
 */
@Data
@Component
@ConfigurationProperties(prefix = "terminal", ignoreInvalidFields = true)
public class TerminalConfigurationProperties {

    @Data
    public static class Audit {

        private String path;
        private Boolean open;

    }

    private Audit audit;

    public interface Suffix {
        String AUDIT_LOG = ".log";
        String COMMAND_LOG = "_commander.log";
        String FMT_COMMAND_LOG = "_commander_fmt.log";   // formatted
    }

    public String buildAuditLogPath(String sessionId, String instanceId) {
        return Joiner.on("/").join(audit.getPath(), sessionId, instanceId + Suffix.AUDIT_LOG);
    }

    public String buildCommanderLogPath(String sessionId, String instanceId) {
        return Joiner.on("/").join(audit.getPath(), sessionId, instanceId + Suffix.COMMAND_LOG);
    }

    public String buildFmtCommanderLogPath(String sessionId, String instanceId) {
        return Joiner.on("/").join(audit.getPath(), sessionId, instanceId + Suffix.FMT_COMMAND_LOG);
    }

}