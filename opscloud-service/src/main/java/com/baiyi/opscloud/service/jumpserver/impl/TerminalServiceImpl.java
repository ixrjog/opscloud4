package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.Terminal;
import com.baiyi.opscloud.mapper.jumpserver.TerminalMapper;
import com.baiyi.opscloud.service.jumpserver.TerminalService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 5:56 下午
 * @Version 1.0
 */
@Service
public class TerminalServiceImpl implements TerminalService {

    @Resource
    private TerminalMapper terminalMapper;

    @Override
    public List<Terminal> queryTerminal() {
        Example example = new Example(Terminal.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isDeleted", false);
        criteria.andEqualTo("isAccepted", true);
        return terminalMapper.selectByExample(example);
    }

}
