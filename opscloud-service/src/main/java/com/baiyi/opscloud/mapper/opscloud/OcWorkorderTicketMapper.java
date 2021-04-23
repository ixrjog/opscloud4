package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcWorkorderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkorderTicketParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcWorkorderTicketMapper extends Mapper<OcWorkorderTicket> {

    List<OcWorkorderTicket> queryMyTicketByParam(WorkorderTicketParam.QueryMyTicketPage pageQuery);

    List<OcWorkorderTicket> queryMyFinalizedTicketByParam(WorkorderTicketParam.QueryMyFinalizedTicketPage pageQuery);

    List<OcWorkorderTicket> queryTicketByParam(WorkorderTicketParam.QueryTicketPage pageQuery);
}