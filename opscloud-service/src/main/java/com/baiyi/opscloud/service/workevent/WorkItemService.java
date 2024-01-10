package com.baiyi.opscloud.service.workevent;

import com.baiyi.opscloud.domain.generator.opscloud.WorkItem;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/5 5:07 PM
 * @Since 1.0
 */
public interface WorkItemService {

    List<WorkItem> listByWorkRoleIdAndParentId(Integer workRoleId, Integer parentId);

    WorkItem getById(Integer id);

    List<WorkItem> listByWorkRoleId(Integer workRoleId);

}