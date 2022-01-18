package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketParam;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface WorkOrderTicketMapper extends Mapper<WorkOrderTicket> {

    WorkOrderTicket getNewTicketByUser(@Param("workOrderKey") String workOrderKey, @Param("username") String username);

    List<WorkOrderTicket> queryPageByParam(WorkOrderTicketParam.MyTicketPageQuery pageQuery);

}