package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:38 上午
 * @Version 1.0
 */
public interface TerminalSessionService {

    void add(TerminalSession terminalSession);

    void update(TerminalSession terminalSession);

    TerminalSession getBySessionId(String sessionId);

    TerminalSession getById(int id);

    DataTable<TerminalSession> queryTerminalSessionPage(TerminalSessionParam.TerminalSessionPageQuery pageQuery);

    int countActiveSessionByParam(String serverHostname, String sessionType);

    List<ReportVO.Report> queryMonth();

    List<ReportVO.Report> statMonthlyBySessionType(String sessionType);

    int statUserTotal();

    int statTotal();

}