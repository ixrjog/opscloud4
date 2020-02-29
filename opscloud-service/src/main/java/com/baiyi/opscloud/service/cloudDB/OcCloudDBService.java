package com.baiyi.opscloud.service.cloudDB;

import com.baiyi.opscloud.domain.generator.OcCloudDb;

/**
 * @Author baiyi
 * @Date 2020/2/29 2:48 下午
 * @Version 1.0
 */
public interface OcCloudDBService {

    OcCloudDb queryOcCloudDbByUniqueKey(int cloudDbType, String dbInstanceId);

    void addOcCloudDb(OcCloudDb ocCloudDb);

    void updateOcCloudDb(OcCloudDb ocCloudDb);
}
