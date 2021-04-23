package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcTerminalSession;
import com.baiyi.opscloud.domain.param.term.TermSessionParam;
import com.baiyi.opscloud.mapper.opscloud.OcTerminalSessionMapper;
import com.baiyi.opscloud.service.terminal.OcTerminalSessionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/5/24 12:18 下午
 * @Version 1.0
 */
@Service
public class OcTerminalSessionServiceImpl implements OcTerminalSessionService {

    @Resource
    private OcTerminalSessionMapper ocTerminalSessionMapper;

    @Override
    public DataTable<OcTerminalSession> queryTerminalSessionByParam(TermSessionParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        List<OcTerminalSession> list = ocTerminalSessionMapper.queryTerminalSessionByParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

    @Override
    public OcTerminalSession queryOcTerminalSessionBySessionId(String sessionId) {
        Example example = new Example(OcTerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("sessionId", sessionId);
        return ocTerminalSessionMapper.selectOneByExample(example);
    }

    @Override
    public List<OcTerminalSession> queryOcTerminalSessionByActive() {
        Example example = new Example(OcTerminalSession.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isClosed", false);
        return ocTerminalSessionMapper.selectByExample(example);
    }

    @Override
    public void addOcTerminalSession(OcTerminalSession ocTerminalSession) {
        ocTerminalSessionMapper.insert(ocTerminalSession);
    }

    @Override
    public void updateOcTerminalSession(OcTerminalSession ocTerminalSession) {
        ocTerminalSessionMapper.updateByPrimaryKey(ocTerminalSession);
    }

}
