package com.baiyi.opscloud.sshcore.handler;

import com.baiyi.opscloud.common.redis.RedisUtil;
import com.baiyi.opscloud.common.redis.TerminalKeyUtil;
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

    private static RedisUtil redisUtil;

    private static TerminalConfig terminalConfig;

    @Autowired
    private void setRedisUtil(RedisUtil redisUtil) {
        AuditRecordHandler.redisUtil = redisUtil;
    }

    @Autowired
    private void setXTerminalConfig(TerminalConfig terminalConfig) {
        AuditRecordHandler.terminalConfig = terminalConfig;
    }

    public static void recordAuditLog(String sessionId, String instanceId,char[] buf) {
        try {
                IOUtil.appendFile(new String(buf), terminalConfig.buildAuditLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端会话日志写入失败! sessionId = {}, instanceId = {}", sessionId, instanceId);
        }
    }

    private static void recordAuditLog(String sessionId, String instanceId) {
        String cacheKey = TerminalKeyUtil.buildAuditLogKey(sessionId, instanceId);
        try {
            if (redisUtil.hasKey(cacheKey)) {
                // 追加内容
                String log = (String) redisUtil.get(cacheKey);
                IOUtil.appendFile(log, terminalConfig.buildAuditLogPath(sessionId, instanceId));
                redisUtil.del(cacheKey); // 清空缓存
            }
        } catch (Exception e) {
            log.error("Web终端会话日志写入失败! sessionId = {}, instanceId = {}", sessionId, instanceId);
        }
    }

    /**
     * 用户命令操作审计日志，暂不使用
     * @param commander
     * @param sessionId
     * @param instanceId
     */
    public static void recordCommanderLog(StringBuffer commander, String sessionId, String instanceId) {
        try {
            String log = new String(commander);
            log.replaceAll("(\n|\r\n)\\s+", "");
            while (log.contains("\b")) {
                log = log.replaceFirst(".\b", ""); // 退格处理
            }
            IOUtil.appendFile(log, terminalConfig.buildCommanderLogPath(sessionId, instanceId));
        } catch (Exception e) {
            log.error("Web终端命令日志写入失败! sessionId = {}, instanceId = {}", sessionId, instanceId);
        }
    }
}
