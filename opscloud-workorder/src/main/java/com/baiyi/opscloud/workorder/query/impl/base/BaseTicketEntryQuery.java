package com.baiyi.opscloud.workorder.query.impl.base;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderTicketEntryParam;
import com.baiyi.opscloud.domain.vo.workorder.WorkOrderTicketVO;
import com.baiyi.opscloud.service.user.UserService;
import com.baiyi.opscloud.workorder.query.ITicketEntryQuery;
import com.baiyi.opscloud.workorder.query.factory.WorkOrderTicketEntryQueryFactory;
import jakarta.annotation.Resource;
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
public abstract class BaseTicketEntryQuery<T> implements ITicketEntryQuery<T>, InitializingBean {

    @Resource
    private UserService userService;

    @Override
    public List<WorkOrderTicketVO.Entry<T>> query(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        preHandle(entryQuery);
        List<T> entries = doFilter(queryEntries(entryQuery));
        return entries.stream().map(e -> toEntry(entryQuery, e)).collect(Collectors.toList());
    }

    /**
     * 重写此方法过滤结果集
     * @param preEntries
     * @return
     */
    protected List<T> doFilter(List<T> preEntries) {
        return preEntries;
    }

    /**
     * 预处理
     *
     * @param entryQuery
     */
    private void preHandle(WorkOrderTicketEntryParam.EntryQuery entryQuery) {
        if (entryQuery.getLength() == 0 || entryQuery.getLength() > 20) {
            entryQuery.setLength(20);
        }
    }

    abstract protected List<T> queryEntries(WorkOrderTicketEntryParam.EntryQuery entryQuery);

    abstract protected WorkOrderTicketVO.Entry<T> toEntry(WorkOrderTicketEntryParam.EntryQuery entryQuery, T entry);

    @Override
    public void afterPropertiesSet() throws Exception {
        WorkOrderTicketEntryQueryFactory.register(this);
    }

    protected User getUser() {
        return userService.getByUsername(SessionHolder.getUsername());
    }

}