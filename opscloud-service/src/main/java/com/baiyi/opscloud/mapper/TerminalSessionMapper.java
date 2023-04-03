package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSession;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TerminalSessionMapper extends Mapper<TerminalSession> {

    List<ReportVO.Report> statMonthlyBySessionType(String sessionType);

    List<ReportVO.Report> queryMonth();

    int statUserTotal();

    int statTotal();

}