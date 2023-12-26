package com.baiyi.opscloud.workorder.helper.strategy.base;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderTicket;

import java.util.Set;

/**
 * @Author baiyi
 * @Date 2022/1/27 4:45 PM
 * @Version 1.0
 */
public interface ISendNotice {

    void send(WorkOrderTicket ticket);

    Set<String> getPhases();

}