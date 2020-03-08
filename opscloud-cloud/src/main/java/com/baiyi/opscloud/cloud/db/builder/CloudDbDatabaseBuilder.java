package com.baiyi.opscloud.cloud.db.builder;

import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;

/**
 * @Author baiyi
 * @Date 2020/3/1 6:33 下午
 * @Version 1.0
 */
public class CloudDbDatabaseBuilder {

    public static OcCloudDbDatabase build(int cloudDbId, DescribeDatabasesResponse.Database database) {
        CloudDbDatabaseBO cloudDbDatabaseBO = CloudDbDatabaseBO.builder()
                .cloudDbId(cloudDbId)
                .dbInstanceId(database.getDBInstanceId())
                .dbName(database.getDBName())
                .engine(database.getEngine())
                .dbDescription(database.getDBDescription())
                .dbStatus(database.getDBStatus())
                .characterSetName(database.getCharacterSetName())
                .build();
        return covert(cloudDbDatabaseBO);
    }

    private static OcCloudDbDatabase covert(CloudDbDatabaseBO cloudDbDatabaseBO) {
        return BeanCopierUtils.copyProperties(cloudDbDatabaseBO, OcCloudDbDatabase.class);
    }
}
