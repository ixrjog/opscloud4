package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:36 AM
 * @Version 1.0
 */
public interface WorkOrderGroupService {

    List<WorkOrderGroup> queryAll();

    WorkOrderGroup getById(int id);

}
