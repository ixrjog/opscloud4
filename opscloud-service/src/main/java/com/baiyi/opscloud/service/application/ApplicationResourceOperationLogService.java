package com.baiyi.opscloud.service.application;

import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResourceOperationLog;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/5 13:05
 * @Version 1.0
 */
public interface ApplicationResourceOperationLogService {

    List<ApplicationResourceOperationLog> queryByResourceId(Integer resourceId, int limit);

    void add(ApplicationResourceOperationLog applicationResourceOperationLog);

    void update(ApplicationResourceOperationLog applicationResourceOperationLog);

    void updateByPrimaryKeySelective(ApplicationResourceOperationLog applicationResourceOperationLog);

}
