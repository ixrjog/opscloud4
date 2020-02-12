package com.baiyi.opscloud.service.server;

import com.baiyi.opscloud.domain.generator.OcCloudserver;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:15 上午
 * @Version 1.0
 */
public interface OcCloudserverService {

    List<OcCloudserver> queryOcCloudserverByType(int cloudserverType);

    OcCloudserver queryOcCloudserverByInstanceId(String instanceId);

    OcCloudserver queryOcCloudserver(int id);

    void addOcCloudserver(OcCloudserver ocCloudserver);

    void updateOcCloudserver(OcCloudserver ocCloudserver);

    void delOcCloudserver(int id);
}
