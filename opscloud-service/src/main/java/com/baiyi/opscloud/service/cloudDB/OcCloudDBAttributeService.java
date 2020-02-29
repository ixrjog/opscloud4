package com.baiyi.opscloud.service.cloudDB;

import com.baiyi.opscloud.domain.generator.OcCloudDbAttribute;

/**
 * @Author baiyi
 * @Date 2020/2/29 8:20 下午
 * @Version 1.0
 */
public interface OcCloudDBAttributeService {

    OcCloudDbAttribute queryOcCloudDbAttributeByUniqueKey(int cloudDbId, String attributeName);

    void addOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute);

    void updateOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute);
}
