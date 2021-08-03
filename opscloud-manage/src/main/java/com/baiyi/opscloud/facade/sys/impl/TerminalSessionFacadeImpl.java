package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionInstanceCommandParam;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionInstanceCommandVO;
import com.baiyi.opscloud.domain.vo.terminal.TerminalSessionVO;
import com.baiyi.opscloud.facade.sys.TerminalSessionFacade;
import com.baiyi.opscloud.packer.sys.TerminalSessionInstanceCommandPacker;
import com.baiyi.opscloud.packer.sys.TerminalSessionPacker;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:31 下午
 * @Version 1.0
 */
@Service
public class TerminalSessionFacadeImpl implements TerminalSessionFacade {

    @Resource
    private TerminalSessionService terminalSessionService;

    @Resource
    private TerminalSessionInstanceCommandPacker terminalSessionInstanceCommandPacker;

    @Resource
    private TerminalSessionInstanceCommandService terminalSessionInstanceCommandService;

    @Resource
    private TerminalSessionPacker terminalSessionPacker;

    @Override
    public DataTable<TerminalSessionVO.Session> queryTerminalSessionPage(TerminalSessionParam.TerminalSessionPageQuery pageQuery) {
        DataTable<TerminalSession> table = terminalSessionService.queryTerminalSessionPage(pageQuery);
        return new DataTable<>(terminalSessionPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

    @Override
    public DataTable<TerminalSessionInstanceCommandVO.Command> queryTerminalSessionCommandPage(TerminalSessionInstanceCommandParam.InstanceCommandPageQuery pageQuery) {
        DataTable<TerminalSessionInstanceCommand> table = terminalSessionInstanceCommandService.queryTerminalSessionInstanceCommandPage(pageQuery);
        return new DataTable<>(terminalSessionInstanceCommandPacker.wrapVOList(table.getData(), pageQuery), table.getTotalNum());
    }

}
