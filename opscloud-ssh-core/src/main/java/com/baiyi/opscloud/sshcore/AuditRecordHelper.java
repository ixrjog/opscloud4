package com.baiyi.opscloud.sshcore;

import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.sshcore.config.TerminalConfigurationProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * @Author baiyi
 * @Date 2020/5/25 3:08 下午
 * @Version 1.0
 */
@Slf4j
@Component
public class AuditRecordHelper {

    private static TerminalConfigurationProperties terminalConfigurationProperties;

    @Autowired
    private void setTerminalConfig(TerminalConfigurationProperties terminalConfigurationProperties) {
        AuditRecordHelper.terminalConfigurationProperties = terminalConfigurationProperties;
    }

    public static void record(String sessionId, String instanceId, char[] buf, int off, int len) {
        try {
            IOUtil.appendFile(new String(buf).substring(off, len), terminalConfigurationProperties.buildAuditLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端会话日志写入失败! sessionId={}, instanceId={}", sessionId, instanceId);
        }
    }

    public static String getAuditLogPath(String sessionId, String instanceId) {
        return terminalConfigurationProperties.buildAuditLogPath(sessionId, instanceId);
    }

    /**
     * 用户命令操作审计日志，暂不使用
     *
     * @param commander
     * @param sessionId
     * @param instanceId
     */
    public static void recordCommanderLog(StringBuffer commander, String sessionId, String instanceId) {
        try {
            String log = new String(commander);
            log = log.replaceAll("(\n|\r\n)\\s+", "");
            while (log.contains("\b")) {
                // 退格处理
                log = log.replaceFirst(".\b", "");
            }
            IOUtil.appendFile(log, terminalConfigurationProperties.buildCommanderLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端命令日志写入失败! sessionId={}, instanceId={}", sessionId, instanceId);
        }
    }

}