package com.baiyi.opscloud.cloud.db.builder;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.common.base.CloudDBType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.common.util.TimeUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:32 下午
 * @Version 1.0
 */
public class CloudDbBuilder {

    public static OcCloudDb build(CloudAccount cloudAccount, DescribeDBInstancesResponse.DBInstance dbInstance) {
        CloudDbBO cloudDbBO = CloudDbBO.builder()
                .uid(cloudAccount.getUid())
                .accountName(cloudAccount.getName())
                .cloudDbType(CloudDBType.ALIYUN_RDS_MYSQL.getType())
                .regionId(dbInstance.getRegionId())
                .dbInstanceId(dbInstance.getDBInstanceId())
                .dbInstanceDescription(dbInstance.getDBInstanceDescription())
                .dbInstanceType(dbInstance.getDBInstanceType())
                .engine(dbInstance.getEngine())
                .engineVersion(dbInstance.getEngineVersion())
                .zone(dbInstance.getZoneId())
                .payType(dbInstance.getPayType())
                .dbInstanceStatus(dbInstance.getDBInstanceStatus())
                .expiredTime(TimeUtils.acqGmtDate(dbInstance.getExpireTime()))
                .instanceNetworkType(dbInstance.getInstanceNetworkType())
                .connectionMode(dbInstance.getConnectionMode())
                .dbInstanceNetType(dbInstance.getDBInstanceNetType())
                .dbInstanceStorageType(dbInstance.getDBInstanceStorageType())
                .dbInstanceClass(dbInstance.getDBInstanceClass())
                .createdTime(TimeUtils.acqGmtDate(dbInstance.getCreateTime()))
                .category(dbInstance.getCategory())
                .build();
        return covert(cloudDbBO);
    }

    private static OcCloudDb covert(CloudDbBO cloudDbBO) {
        return BeanCopierUtils.copyProperties(cloudDbBO, OcCloudDb.class);
    }
}
