package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAttribute;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/29 8:20 下午
 * @Version 1.0
 */
public interface OcCloudDBAttributeService {

    OcCloudDbAttribute queryOcCloudDbAttributeByUniqueKey(int cloudDbId, String attributeName);

    List<OcCloudDbAttribute> queryOcCloudDbAttributeByCloudDbId(int cloudDbId);

    void addOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute);

    void updateOcCloudDbAttribute(OcCloudDbAttribute ocCloudDbAttribute);

    void delOcCloudDbAttributeById(int id);
}
