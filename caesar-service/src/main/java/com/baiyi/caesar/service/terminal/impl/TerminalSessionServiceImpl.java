package com.baiyi.caesar.service.terminal.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSession;
import com.baiyi.caesar.mapper.caesar.TerminalSessionMapper;
import com.baiyi.caesar.service.terminal.TerminalSessionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:38 上午
 * @Version 1.0
 */
@Service
public class TerminalSessionServiceImpl implements TerminalSessionService {

    @Resource
    private TerminalSessionMapper sessionMapper;

    @Override
    public void add(TerminalSession terminalSession) {
        sessionMapper.insert(terminalSession);
    }

    @Override
    public void update(TerminalSession terminalSession) {
        sessionMapper.updateByPrimaryKey(terminalSession);
    }

}
