package com.baiyi.opscloud.xterm.config;

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
@ConfigurationProperties(prefix = "xterm", ignoreInvalidFields = true)
public class XTermConfig {

    private String auditPath;
    private Boolean openAudit;

    public String getAuditLogPath(String sessionId,String instanceId){
        return Joiner.on("/").join(auditPath,sessionId,instanceId + ".log");
    }

    public String getCommanderLogPath(String sessionId,String instanceId){
        return Joiner.on("/").join(auditPath,sessionId,instanceId + "_commander.log");
    }
}
