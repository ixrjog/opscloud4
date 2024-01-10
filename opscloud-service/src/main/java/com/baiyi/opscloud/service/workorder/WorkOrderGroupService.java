package com.baiyi.opscloud.service.workorder;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkOrderGroup;
import com.baiyi.opscloud.domain.param.workorder.WorkOrderGroupParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/1/6 10:36 AM
 * @Version 1.0
 */
public interface WorkOrderGroupService {

    DataTable<WorkOrderGroup> queryPageByParam(WorkOrderGroupParam.WorkOrderGroupPageQuery pageQuery);

    List<WorkOrderGroup> queryAll();

    WorkOrderGroup getById(int id);

    void add(WorkOrderGroup workOrderGroup);

    void update(WorkOrderGroup workOrderGroup);

    void deleteById(int id);

    int count();

}