package com.baiyi.opscloud.facade.terminal.impl;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.terminal.TerminalReportVO;
import com.baiyi.opscloud.facade.terminal.TerminalReportFacade;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1H, key = "'opscloud.v4.report#statTerminalReport'")
    public TerminalReportVO.TerminalReport statTerminalReport() {
        return TerminalReportVO.TerminalReport.builder()
                .userTotal(terminalSessionService.statUserTotal())
                .sessionTotal(terminalSessionService.statTotal())
                .instanceTotal(terminalSessionInstanceService.statTotal())
                .commandTotal(terminalSessionInstanceCommandService.statTotal())
                .sessionReport(buildSessionReport())
                .instanceReport(buildInstanceReport())
                .commandMonthReport(ReportVO.MonthReport.buildMonthReport(terminalSessionInstanceCommandService.statByMonth()))
                .build();
    }

    private ReportVO.MonthlyReport buildSessionReport() {
        ReportVO.MonthlyReport monthlyReport = ReportVO.MonthlyReport.builder()
                .dateCat(terminalSessionService.queryMonth().stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build();
        for (SessionTypeEnum value : SessionTypeEnum.values()) {
            List<ReportVO.Report> reports = terminalSessionService.statMonthlyBySessionType(value.name());
            ReportVO.MonthlyReport.put(monthlyReport, value.name(), reports);
        }
        return monthlyReport;
    }

    private ReportVO.MonthlyReport buildInstanceReport() {
        ReportVO.MonthlyReport monthlyReport = ReportVO.MonthlyReport.builder()
                .dateCat(terminalSessionService.queryMonth().stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build();
        for (InstanceSessionTypeEnum value : InstanceSessionTypeEnum.values()) {
            List<ReportVO.Report> reports = terminalSessionInstanceService.statMonthlyByInstanceSessionType(value.name());
            ReportVO.MonthlyReport.put(monthlyReport, value.name(), reports);
        }
        return monthlyReport;
    }

}