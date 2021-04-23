package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcHelpdeskReport;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByType;
import com.baiyi.opscloud.domain.vo.dashboard.HeplDeskGroupByWeek;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.common.special.InsertListMapper;

import java.util.List;

public interface OcHelpdeskReportMapper extends Mapper<OcHelpdeskReport>, InsertListMapper<OcHelpdeskReport> {

    List<HeplDeskGroupByWeek> queryHelpdeskGroupByWeek();

    List<HeplDeskGroupByType> queryHelpdeskGroupByType();
}