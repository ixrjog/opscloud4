package com.baiyi.opscloud.service.terminal.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.domain.param.terminal.TerminalSessionInstanceCommandParam;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import com.baiyi.opscloud.mapper.TerminalSessionInstanceCommandMapper;
import com.baiyi.opscloud.service.terminal.TerminalSessionInstanceCommandService;
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
 * @Date 2021/7/28 10:50 上午
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@RequiredArgsConstructor
public class TerminalSessionInstanceCommandServiceImpl implements TerminalSessionInstanceCommandService {

    private final TerminalSessionInstanceCommandMapper commandMapper;

    @Override
    public void add(TerminalSessionInstanceCommand command) {
        if (!StringUtils.isEmpty(command.getPrompt())) {
            String p = command.getPrompt();
            if (p.length() > 2048) {
                command.setPrompt(p.substring(0, 2048));
            }
        }
        commandMapper.insert(command);
    }

    @Override
    public List<TerminalSessionInstanceCommand> queryByInstanceId(Integer terminalSessionInstanceId) {
        Example example = new Example(TerminalSessionInstanceCommand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("terminalSessionInstanceId", terminalSessionInstanceId);
        return commandMapper.selectByExample(example);
    }

    @Override
    public int countByTerminalSessionInstanceId(Integer terminalSessionInstanceId) {
        Example example = new Example(TerminalSessionInstanceCommand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("terminalSessionInstanceId", terminalSessionInstanceId);
        return commandMapper.selectCountByExample(example);
    }

    @Override
    public DataTable<TerminalSessionInstanceCommand> queryTerminalSessionInstanceCommandPage(TerminalSessionInstanceCommandParam.InstanceCommandPageQuery pageQuery) {
        Page<?> page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength());
        Example example = new Example(TerminalSessionInstanceCommand.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("terminalSessionInstanceId", pageQuery.getTerminalSessionInstanceId());
        if (StringUtils.isNotBlank(pageQuery.getQueryName())) {
            Example.Criteria criteria2 = example.createCriteria();
            String likeName = SQLUtil.toLike(pageQuery.getQueryName());
            criteria2.orLike("input", likeName)
                    .orLike("inputFormatted", likeName);
            example.and(criteria2);
        }
        List<TerminalSessionInstanceCommand> data = commandMapper.selectByExample(example);
        return new DataTable<>(data, page.getTotal());
    }

    @Override
    public List<ReportVO.Report> statByMonth() {
        return commandMapper.statByMonth();
    }

    @Override
    public int statTotal() {
        return commandMapper.statTotal();
    }

}