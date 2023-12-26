package com.baiyi.opscloud.service.workevent;

import com.baiyi.opscloud.domain.generator.opscloud.WorkEventProperty;

import java.util.List;

/**
 * @Author 修远
 * @Date 2022/8/18 6:16 PM
 * @Since 1.0
 */
public interface WorkEventPropertyService {

    void addList(List<WorkEventProperty> workEventPropertyList);

    void update(WorkEventProperty workEventProperty);

    List<WorkEventProperty> listByWorkEventId(Integer workEventId);

    void deleteByWorkEventId(Integer workEventId);

}