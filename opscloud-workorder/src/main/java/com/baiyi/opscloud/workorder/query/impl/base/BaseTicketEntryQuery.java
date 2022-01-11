package com.baiyi.opscloud.workorder.query.impl.base;

import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.workorder.query.ITicketEntryQuery;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/1/11 3:48 PM
 * @Version 1.0
 */
@Slf4j
public abstract class BaseTicketEntryQuery<T> implements ITicketEntryQuery, InitializingBean {

    @Override
    public List<WorkOrderTicketVO.Entry> query(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        preHandle(entryQuery);
        List<T> entries = queryEntries(entryQuery);
        return entries.stream().map(e -> toEntry(entryQuery, e)).collect(Collectors.toList());
    }

    /**
     * 预处理
     *
     * @param entryQuery
     */
    private void preHandle(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        if (entryQuery.getLength() == 0 || entryQuery.getLength() > 20)
            entryQuery.setLength(20);
    }

    abstract protected List<T> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    abstract protected WorkOrderTicketVO.Entry toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, T entry);

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketEntryQueryFactory.register(this);
    }

}
