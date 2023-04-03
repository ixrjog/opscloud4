package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstanceCommand;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TerminalSessionInstanceCommandMapper extends Mapper<TerminalSessionInstanceCommand> {

    List<ReportVO.Report> statByMonth();

    int statTotal();

}