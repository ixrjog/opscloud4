package com.baiyi.opscloud.workorder.query;

import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/11 3:45 PM
 * @Version 1.0
 */
public interface ITicketEntryQuery<T> {

    /**
     * 查询工单条目
     *
     * @param entryQuery
     * @return
     */
    List<WorkOrderTicketVO.Entry<T>> query(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    /**
     * 工单Key
     *
     * @return
     */
    String getKey();

}