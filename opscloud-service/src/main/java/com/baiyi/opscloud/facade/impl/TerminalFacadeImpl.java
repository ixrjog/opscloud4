package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.facade.TerminalFacade;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:26 下午
 * @Version 1.0
 */
@Service
public class TerminalFacadeImpl  implements TerminalFacade {

    @Resource
    private OcTerminalSessionService ocTerminalSessionService;

    @Override
    public void addOcTerminalSession(OcTerminalSession ocTerminalSession){
        ocTerminalSessionService.addOcTerminalSession(ocTerminalSession);
    }

    @Override
    public  void updateOcTerminalSession(OcTerminalSession ocTerminalSession){
        ocTerminalSessionService.updateOcTerminalSession(ocTerminalSession);
    }

}
