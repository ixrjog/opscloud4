package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroupProperty;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/6/4 10:29 上午
 * @Version 1.0
 */
public interface OcServerGroupPropertyService {

    OcServerGroupProperty queryOcServerGroupPropertyByUniqueKey(OcServerGroupProperty ocServerGroupProperty);

    List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupId(int serverGroupId);

    List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupIdAndEnvType(int serverGroupId, int envType);

    List<OcServerGroupProperty> queryOcServerGroupPropertyByServerGroupIdAndEnvTypeAnd(int serverGroupId, int envType, String propertyName);

    void addOcServerGroupProperty(OcServerGroupProperty ocServerGroupProperty);

    void updateOcServerGroupProperty(OcServerGroupProperty ocServerGroupProperty);

    void deleteOcServerGroupPropertyById(int id);


}
