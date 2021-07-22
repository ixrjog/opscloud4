package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.mapper.opscloud.TerminalSessionMapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

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

    @Override
    public TerminalSession getBySessionId(String sessionId) {
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sessionMapper.selectOneByExample(example);
    }

}
