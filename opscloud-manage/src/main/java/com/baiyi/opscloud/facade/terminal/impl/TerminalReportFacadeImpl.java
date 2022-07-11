package com.baiyi.opscloud.facade.terminal.impl;

import com.baiyi.opscloud.common.config.CachingConfiguration;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.domain.vo.terminal.TerminalReportVO;
import com.baiyi.opscloud.facade.terminal.TerminalReportFacade;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.sshcore.enums.InstanceSessionTypeEnum;
import com.baiyi.opscloud.sshcore.enums.SessionTypeEnum;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
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
    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_1HOUR, key = "'opscloud.v4.report#statTerminalReport'")
    public TerminalReportVO.TerminalReport statTerminalReport() {
        return TerminalReportVO.TerminalReport.builder()
                .userTotal(terminalSessionService.statUserTotal())
                .sessionTotal(terminalSessionService.statTotal())
                .instanceTotal(terminalSessionInstanceService.statTotal())
                .commandTotal(terminalSessionInstanceCommandService.statTotal())
                .sessionReport(buildSessionReport())
                .instanceReport(buildInstanceReport())
                .commandMonthReport(TerminalReportVO.MonthReport.buildMonthReport(terminalSessionInstanceCommandService.statByMonth()))
                .build();
    }

    private TerminalReportVO.MonthlyReport buildSessionReport() {
        TerminalReportVO.MonthlyReport monthlyReport = TerminalReportVO.MonthlyReport.builder()
                .dateCat(terminalSessionService.queryMonth().stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build();
        for (SessionTypeEnum value : SessionTypeEnum.values()) {
            List<ReportVO.Report> reports = terminalSessionService.statMonthlyBySessionType(value.name());
            assembleMonthlyReport(monthlyReport, value.name(), reports);
        }
        return monthlyReport;
    }

    private TerminalReportVO.MonthlyReport buildInstanceReport() {
        TerminalReportVO.MonthlyReport monthlyReport = TerminalReportVO.MonthlyReport.builder()
                .dateCat(terminalSessionService.queryMonth().stream().map(ReportVO.Report::getCName).collect(Collectors.toList()))
                .build();
        for (InstanceSessionTypeEnum value : InstanceSessionTypeEnum.values()) {
            List<ReportVO.Report> reports = terminalSessionInstanceService.statMonthlyByInstanceSessionType(value.name());
            assembleMonthlyReport(monthlyReport, value.name(), reports);
        }
        return monthlyReport;
    }

    private void assembleMonthlyReport(TerminalReportVO.MonthlyReport monthlyReport, String name, List<ReportVO.Report> reports) {
        if (CollectionUtils.isEmpty(reports)) return;
        Map<String, ReportVO.Report> reportMap = reports.stream().collect(Collectors.toMap(ReportVO.Report::getCName, a -> a, (k1, k2) -> k1));
        List<Integer> values = Lists.newArrayList();
        monthlyReport.getDateCat().forEach(s -> values.add(reportMap.containsKey(s) ? reportMap.get(s).getValue() : Integer.valueOf(0)));
        monthlyReport.getValueMap().put(name, values);
    }

}
