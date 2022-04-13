package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.mapper.opscloud.TerminalSessionMapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionService;
import com.baiyi.opscloud.util.SQLUtil;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/5/28 9:38 上午
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class TerminalSessionServiceImpl implements TerminalSessionService {

    private final TerminalSessionMapper sessionMapper;

    @Override
    public void add(TerminalSession terminalSession) {
        sessionMapper.insert(terminalSession);
    }

    @Override
    public void update(TerminalSession terminalSession) {
        sessionMapper.updateByPrimaryKey(terminalSession);
    }

    @Override
    public DataTable<TerminalSession> queryTerminalSessionPage(TerminalSessionParam.TerminalSessionPageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getUsername())) {
            criteria.andLike("username", SQLUtil.toLike(pageQuery.getUsername()));
        }else{
            criteria.andIsNotNull("username");
        }
        if (!StringUtils.isEmpty(pageQuery.getSessionType()))
            criteria.andEqualTo("sessionType", pageQuery.getSessionType());
        example.setOrderByClause("create_time desc");
        List<TerminalSession> data = sessionMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public TerminalSession getBySessionId(String sessionId) {
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sessionMapper.selectOneByExample(example);
    }

}
