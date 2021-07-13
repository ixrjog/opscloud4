package com.baiyi.opscloud.service.application;

import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/13 9:30 上午
 * @Version 1.0
 */
public interface ApplicationResourceService {

    void add(ApplicationResource applicationResource);

    void update(ApplicationResource applicationResource);

    List<ApplicationResource> queryByApplication(Integer applicationId);

    List<ApplicationResource> queryByApplication(Integer applicationId, String resourceType);

    List<ApplicationResource> queryByApplication(Integer applicationId, String resourceType, int businessType);

}
