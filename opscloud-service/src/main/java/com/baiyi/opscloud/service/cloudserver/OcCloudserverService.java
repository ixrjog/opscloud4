package com.baiyi.opscloud.service.cloudserver;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.OcCloudserver;
import com.baiyi.opscloud.domain.param.cloudserver.CloudserverParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:15 上午
 * @Version 1.0
 */
public interface OcCloudserverService {

    DataTable<OcCloudserver> queryOcCloudserverByParam(CloudserverParam.PageQuery pageQuery);

    List<OcCloudserver> queryOcCloudserverByType(int cloudserverType);

    OcCloudserver queryOcCloudserverByInstanceId(String instanceId);

    OcCloudserver queryOcCloudserverById(int id);

    void addOcCloudserver(OcCloudserver ocCloudserver);

    void updateOcCloudserver(OcCloudserver ocCloudserver);

    void deleteOcCloudserverById(int id);
}
