package com.baiyi.opscloud.leo.log;

import com.baiyi.opscloud.common.util.StringFormatter;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuild;
import com.baiyi.opscloud.domain.generator.opscloud.LeoBuildLog;
import com.baiyi.opscloud.leo.constants.LogTypeConstants;
import com.baiyi.opscloud.service.leo.LeoBuildLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.event.Level;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/11/9 14:26
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LeoBuildingLog {

    private final LeoBuildLogService logService;

    public void info(LeoBuild leoBuild, String log, Object... var2) {
        saveBuildLog(leoBuild.getId(), Level.INFO, LogTypeConstants.DEF, log, var2);
    }

    public void warn(LeoBuild leoBuild, String log, Object... var2) {
        saveBuildLog(leoBuild.getId(), Level.WARN, LogTypeConstants.DEF, log, var2);
    }

    public void error(LeoBuild leoBuild, String log, Object... var2) {
        saveBuildLog(leoBuild.getId(), Level.ERROR, LogTypeConstants.DEF, log, var2);
    }

    public void debug(LeoBuild leoBuild, String log, Object... var2) {
        saveBuildLog(leoBuild.getId(), Level.DEBUG, LogTypeConstants.DEF, log, var2);
    }

    public void debug(LeoBuild leoBuild, LogTypeConstants logType, String log, Object... var2) {
        saveBuildLog(leoBuild.getId(), Level.DEBUG, logType, log, var2);
    }

    /**
     * 保存日志
     *
     * @param buildId
     * @param level
     * @param log
     * @param var2
     */
    private void saveBuildLog(int buildId, Level level, LogTypeConstants logType, String log, Object... var2) {
        LeoBuildLog buildLog = LeoBuildLog.builder()
                .buildId(buildId)
                .logLevel(level.name())
                .logType(logType.name())
                .log(StringFormatter.arrayFormat(log, var2))
                .build();
        logService.add(buildLog);
    }

}