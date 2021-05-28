package com.baiyi.caesar.service.terminal.impl;

import com.baiyi.caesar.domain.generator.caesar.TerminalSessionInstance;
import com.baiyi.caesar.mapper.caesar.TerminalSessionInstanceMapper;
import com.baiyi.caesar.service.terminal.TerminalSessionInstanceService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/5/28 11:28 上午
 * @Version 1.0
 */
@Service
public class TerminalSessionInstanceServiceImpl implements TerminalSessionInstanceService {

    @Resource
    private TerminalSessionInstanceMapper sessionInstanceMapper;

    @Override
    public void add(TerminalSessionInstance terminalSessionInstance){
        sessionInstanceMapper.insert(terminalSessionInstance);
    }

    @Override
    public void update(TerminalSessionInstance terminalSessionInstance){
        sessionInstanceMapper.updateByPrimaryKey(terminalSessionInstance);
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
