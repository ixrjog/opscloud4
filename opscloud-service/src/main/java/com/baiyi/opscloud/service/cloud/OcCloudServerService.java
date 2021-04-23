package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.param.cloud.CloudServerParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/1/10 10:15 上午
 * @Version 1.0
 */
public interface OcCloudServerService {

    DataTable<OcCloudServer> queryOcCloudServerByParam(CloudServerParam.CloudServerPageQuery pageQuery);

    DataTable<OcCloudServer> queryOcCloudServerChargeByParam(CloudServerParam.CloudServerChargePageQuery pageQuery);

    List<OcCloudServer> queryOcCloudServerByType(int cloudserverType);

    OcCloudServer queryOcCloudServerByInstanceId(String instanceId);

    OcCloudServer queryOcCloudServerByUnqueKey(int cloudServerType,int serverId);

    OcCloudServer queryOcCloudServerById(int id);

    void addOcCloudServer(OcCloudServer ocCloudserver);

    void updateOcCloudServer(OcCloudServer ocCloudserver);

    void deleteOcCloudServerById(int id);
}
