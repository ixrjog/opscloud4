package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerAttribute;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/6 4:41 下午
 * @Version 1.0
 */
public interface OcServerAttributeService {

    OcServerAttribute queryOcServerAttributeByUniqueKey(OcServerAttribute ocServerAttribute);

    List<OcServerAttribute> queryOcServerAttributeByBusinessTypeAndBusinessId(int businessType, int businessId);

    void addOcServerAttribute(OcServerAttribute ocServerAttribute);

    void updateOcServerAttribute(OcServerAttribute ocServerAttribute);

    void deleteOcServerAttributeById(int id);
}
