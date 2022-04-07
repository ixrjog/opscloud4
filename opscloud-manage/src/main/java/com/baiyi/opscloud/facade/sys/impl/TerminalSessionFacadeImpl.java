package com.baiyi.opscloud.facade.sys.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/22 4:31 下午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TerminalSessionFacadeImpl implements TerminalSessionFacade {

    private final TerminalSessionService terminalSessionService;

    private final TerminalSessionInstanceCommandPacker terminalSessionInstanceCommandPacker;

    private final TerminalSessionInstanceCommandService terminalSessionInstanceCommandService;

    private final TerminalSessionPacker terminalSessionPacker;

    @Override
    public DataTable<TerminalSessionVO.Session> queryTerminalSessionPage(TerminalSessionParam.TerminalSessionPageQuery pageQuery) {
        DataTable<TerminalSession> table = terminalSessionService.queryTerminalSessionPage(pageQuery);
        List<TerminalSessionVO.Session> data = BeanCopierUtil.copyListProperties(table.getData(), TerminalSessionVO.Session.class).stream()
                .peek(e -> terminalSessionPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

    @Override
    public DataTable<TerminalSessionInstanceCommandVO.Command> queryTerminalSessionCommandPage(TerminalSessionInstanceCommandParam.InstanceCommandPageQuery pageQuery) {
        DataTable<TerminalSessionInstanceCommand> table = terminalSessionInstanceCommandService.queryTerminalSessionInstanceCommandPage(pageQuery);
        List<TerminalSessionInstanceCommandVO.Command> data = BeanCopierUtil.copyListProperties(table.getData(), TerminalSessionInstanceCommandVO.Command.class).stream()
                .peek(e -> terminalSessionInstanceCommandPacker.wrap(e, pageQuery)).collect(Collectors.toList());
        return new DataTable<>(data, table.getTotalNum());
    }

}
