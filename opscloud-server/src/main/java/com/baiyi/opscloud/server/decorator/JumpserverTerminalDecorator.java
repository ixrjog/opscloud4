package com.baiyi.opscloud.server.decorator;

import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverTerminalVO;
import com.baiyi.opscloud.service.jumpserver.TerminalSessionService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/13 6:05 下午
 * @Version 1.0
 */
@Component
public class JumpserverTerminalDecorator {

    @Resource
    private TerminalSessionService terminalSessionService;

    public JumpserverTerminalVO.Terminal decorator(JumpserverTerminalVO.Terminal terminal) {
        // 装饰当前会话
        terminal.setSessions(terminalSessionService.countTerminalSession(terminal.getId()));
        return terminal;
    }
}
