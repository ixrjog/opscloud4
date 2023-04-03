package com.baiyi.opscloud.mapper;

import com.baiyi.opscloud.domain.generator.opscloud.TerminalSessionInstance;
import com.baiyi.opscloud.domain.vo.base.ReportVO;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface TerminalSessionInstanceMapper extends Mapper<TerminalSessionInstance> {

    List<ReportVO.Report> statMonthlyByInstanceSessionType(String instanceSessionType);

    List<ReportVO.Report> queryMonth();

    int statTotal();

}