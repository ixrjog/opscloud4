package com.baiyi.opscloud.service.jumpserver.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.jumpserver.TerminalSession;
import com.baiyi.opscloud.domain.param.PageParam;
import com.baiyi.opscloud.mapper.jumpserver.TerminalSessionMapper;
import com.baiyi.opscloud.service.jumpserver.TerminalSessionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

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

    @Override
    public DataTable<TerminalSession> queryTerminalSessionPage(PageParam pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<TerminalSession> terminalSessionList= terminalSessionMapper.queryTerminalSessionPage(pageQuery);
        return new DataTable<>(terminalSessionList, page.getTotal());
    }

}
