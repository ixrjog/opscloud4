package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.mapper.opscloud.TerminalSessionInstanceCommandMapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/28 10:50 上午
 * @Version 1.0
 */
@Service
public class TerminalSessionInstanceCommandServiceImpl implements TerminalSessionInstanceCommandService {

    @Resource
    private TerminalSessionInstanceCommandMapper commandMapper;

    @Override
    public void add(TerminalSessionInstanceCommand command) {
        commandMapper.insert(command);
    }


    @Override
    public List<TerminalSessionInstanceCommand> queryByInstanceId(Integer terminalSessionInstanceId) {
        Example example = new Example(TerminalSessionInstance.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("terminalSessionInstanceId", terminalSessionInstanceId);
        return commandMapper.selectByExample(example);
    }


}
