package com.baiyi.opscloud.facade.terminal.impl;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.vo.terminal.TerminalReportVO;
import com.baiyi.opscloud.facade.terminal.TerminalReportFacade;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/7/7 21:14
 * @Version 1.0
 */
class TerminalReportFacadeImplTest extends BaseUnit {

    @Resource
    private TerminalReportFacade terminalReportFacade;

    @Test
    void reportTest(){
        TerminalReportVO.TerminalReport terminalReport = terminalReportFacade.statTerminalReport();
        print(terminalReport);
    }

}