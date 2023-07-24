package com.baiyi.opscloud.leo.log;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeploy;
import com.baiyi.opscloud.domain.generator.opscloud.LeoDeployLog;
import com.baiyi.opscloud.leo.constants.LogTypeConstants;
import com.baiyi.opscloud.service.leo.LeoDeployLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/12/5 20:29
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoDeployingLog {

    private final LeoDeployLogService logService;

    public void info(LeoDeploy leoDeploy, String log, Object... var2) {
        saveDeployLog(leoDeploy.getId(), Level.INFO, LogTypeConstants.DEF, log, var2);
    }

    public void warn(LeoDeploy leoDeploy, String log, Object... var2) {
        saveDeployLog(leoDeploy.getId(), Level.WARN, LogTypeConstants.DEF, log, var2);
    }

    public void error(LeoDeploy leoDeploy, String log, Object... var2) {
        saveDeployLog(leoDeploy.getId(), Level.ERROR, LogTypeConstants.DEF, log, var2);
    }

    public void debug(LeoDeploy leoDeploy, String log, Object... var2) {
        saveDeployLog(leoDeploy.getId(), Level.DEBUG, LogTypeConstants.DEF, log, var2);
    }

    public void debug(LeoDeploy leoDeploy, LogTypeConstants logType, String log, Object... var2) {
        saveDeployLog(leoDeploy.getId(), Level.DEBUG, logType, log, var2);
    }

    /**
     * 保存日志
     *
     * @param deployId
     * @param level
     * @param logType
     * @param log
     * @param var2
     */
    private void saveDeployLog(int deployId, Level level, LogTypeConstants logType, String log, Object... var2) {
        LeoDeployLog deployLog = LeoDeployLog.builder()
                .deployId(deployId)
                .logLevel(level.name())
                .logType(logType.name())
                .log(StringFormatter.arrayFormat(log, var2))
                .build();
        logService.add(deployLog);
    }

}