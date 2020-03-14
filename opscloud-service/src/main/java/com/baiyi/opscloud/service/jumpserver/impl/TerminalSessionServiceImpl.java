package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.generator.jumpserver.TerminalSession;
import com.baiyi.opscloud.mapper.jumpserver.TerminalSessionMapper;
import com.baiyi.opscloud.service.jumpserver.TerminalSessionService;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/3/13 6:08 下午
 * @Version 1.0
 */
@Service
public class TerminalSessionServiceImpl implements TerminalSessionService {

    @Resource
    private TerminalSessionMapper terminalSessionMapper;

    @Override
    public int countTerminalSession(String terminalId){
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("terminalId", terminalId);
        criteria.andEqualTo("isFinished", false);
        return terminalSessionMapper.selectCountByExample(example);

    }


}
