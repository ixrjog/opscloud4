package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.mapper.TerminalSessionMapper;
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
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
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
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(pageQuery.getUsername())) {
            criteria.andLike("username", SQLUtil.toLike(pageQuery.getUsername()));
        }
        if (!StringUtils.isEmpty(pageQuery.getSessionType())) {
            criteria.andEqualTo("sessionType", pageQuery.getSessionType());
        }
        if (pageQuery.getSessionClosed() != null) {
            criteria.andEqualTo("sessionClosed", pageQuery.getSessionClosed());
        }
        if (StringUtils.isNotBlank(pageQuery.getServerHostname())) {
            criteria.andEqualTo("serverHostname", pageQuery.getServerHostname());
        }
        example.setOrderByClause("create_time desc");
        List<TerminalSession> data = sessionMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public int countActiveSessionByParam(String serverHostname, String sessionType) {
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("serverHostname", serverHostname)
                .andEqualTo("sessionClosed", false)
                .andEqualTo("sessionType", sessionType);
        return sessionMapper.selectCountByExample(example);
    }

    @Override
    public TerminalSession getById(int id) {
        return sessionMapper.selectByPrimaryKey(id);
    }

    @Override
    public TerminalSession getBySessionId(String sessionId) {
        Example example = new Example(TerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return sessionMapper.selectOneByExample(example);
    }

    @Override
    public List<ReportVO.Report> statMonthlyBySessionType(String sessionType) {
        return sessionMapper.statMonthlyBySessionType(sessionType);
    }

    @Override
    public List<ReportVO.Report> queryMonth() {
        return sessionMapper.queryMonth();
    }

    @Override
    public int statUserTotal() {
        return sessionMapper.statUserTotal();
    }

    @Override
    public int statTotal() {
        return sessionMapper.statTotal();
    }

}