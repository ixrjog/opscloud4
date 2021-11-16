package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.mapper.opscloud.TerminalSessionInstanceMapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:28 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TerminalSessionInstanceServiceImpl implements TerminalSessionInstanceService {

    private final TerminalSessionInstanceMapper sessionInstanceMapper;

    @Override
    public void add(TerminalSessionInstance terminalSessionInstance){
        sessionInstanceMapper.insert(terminalSessionInstance);
    }

    @Override
    public void update(TerminalSessionInstance terminalSessionInstance){
        sessionInstanceMapper.updateByPrimaryKey(terminalSessionInstance);
    }

    @Override
    public List<TerminalSessionInstance> queryBySessionId(String sessionId){
        Example example = new Example(TerminalSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sessionInstanceMapper.selectByExample(example);
    }

    @Override
    public TerminalSessionInstance getByUniqueKey(String sessionId, String instanceId){
        Example example = new Example(TerminalSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        criteria.andEqualTo("instanceId", instanceId);
        return sessionInstanceMapper.selectOneByExample(example);
    }
}
