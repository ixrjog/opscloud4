package com.baiyi.opscloud.service.terminal;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.vo.base.ReportVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:28 上午
 * @Version 1.0
 */
public interface TerminalSessionInstanceService {

    void add(TerminalSessionInstance terminalSessionInstance);

    void update(TerminalSessionInstance terminalSessionInstance);

    List<TerminalSessionInstance> queryBySessionId(String sessionId);

    TerminalSessionInstance getByUniqueKey(String sessionId,String instanceId);

    List<ReportVO.Report> queryMonth();

    List<ReportVO.Report> statMonthlyByInstanceSessionType(String instanceSessionType);

    int statTotal();

}