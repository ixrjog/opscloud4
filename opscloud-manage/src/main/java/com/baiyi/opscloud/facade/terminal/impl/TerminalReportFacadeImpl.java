package com.baiyi.opscloud.facade.terminal.impl;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.domain.vo.terminal.TerminalReportVO;
import com.baiyi.opscloud.facade.terminal.TerminalReportFacade;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2022/7/7 20:50
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class TerminalReportFacadeImpl implements TerminalReportFacade {

    private final TerminalSessionService terminalSessionService;

    private final TerminalSessionInstanceService terminalSessionInstanceService;

    private final TerminalSessionInstanceCommandService terminalSessionInstanceCommandService;

    @Override
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_1HOUR, key = "'opscloud.v4.report#statByMonth'")
    public TerminalReportVO.TerminalReport statByMonth() {
        return TerminalReportVO.TerminalReport.builder()
                .sessionMonthReport(TerminalReportVO.MonthReport.buildMonthReport(terminalSessionService.statByMonth()))
                .instanceMonthReport(TerminalReportVO.MonthReport.buildMonthReport(terminalSessionInstanceService.statByMonth()))
                .commandMonthReport(TerminalReportVO.MonthReport.buildMonthReport(terminalSessionInstanceCommandService.statByMonth()))
                .build();
    }

}
