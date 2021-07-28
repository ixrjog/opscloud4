package com.baiyi.opscloud.sshcore.handler;

import com.baiyi.opscloud.common.util.IOUtil;
import com.baiyi.opscloud.sshcore.config.TerminalConfig;
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
public class AuditRecordHandler {

    // private static RedisUtil redisUtil;

    private static TerminalConfig terminalConfig;

//    @Autowired
//    private void setRedisUtil(RedisUtil redisUtil) {
//        AuditRecordHandler.redisUtil = redisUtil;
//    }

    @Autowired
    private void setTerminalConfig(TerminalConfig terminalConfig) {
        AuditRecordHandler.terminalConfig = terminalConfig;
    }

    public static void recordAuditLog(String sessionId, String instanceId, char[] buf, int off, int len) {
        try {
            IOUtil.appendFile(new String(buf).substring(off, len), terminalConfig.buildAuditLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端会话日志写入失败! sessionId = {}, instanceId = {}", sessionId, instanceId);
        }
    }

    public static String getAuditLogPath(String sessionId, String instanceId) {
        return terminalConfig.buildAuditLogPath(sessionId, instanceId);
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
                log = log.replaceFirst(".\b", ""); // 退格处理
            }
            IOUtil.appendFile(log, terminalConfig.buildCommanderLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端命令日志写入失败! sessionId = {}, instanceId = {}", sessionId, instanceId);
        }
    }
}
