package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.OcAuthResource;

/**
 * @Author baiyi
 * @Date 2020/2/12 2:05 下午
 * @Version 1.0
 */
public interface OcAuthResourceService {

    OcAuthResource queryOcAuthResourceByName(String resourceName);

}
