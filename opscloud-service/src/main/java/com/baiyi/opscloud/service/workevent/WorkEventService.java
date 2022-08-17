package com.baiyi.opscloud.service.workevent;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.WorkEvent;
import com.baiyi.opscloud.domain.param.workevent.WorkEventParam;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 4:21 PM
 * @Since 1.0
 */
public interface WorkEventService {

    void update(WorkEvent workEvent);

    void addList(List<WorkEvent> workEventList);

    void deleteById(Integer id);

    DataTable<WorkEvent> queryPageByParam(WorkEventParam.PageQuery pageQuery);
}
