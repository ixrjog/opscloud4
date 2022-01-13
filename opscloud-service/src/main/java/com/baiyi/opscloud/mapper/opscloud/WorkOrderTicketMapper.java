package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;
import org.apache.ibatis.annotations.Param;
import tk.mybatis.mapper.common.Mapper;

public interface WorkOrderTicketMapper extends Mapper<WorkOrderTicket> {

    WorkOrderTicket getNewTicketByUser(@Param("workOrderKey") String workOrderKey, @Param("username") String username);

}