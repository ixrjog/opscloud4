package com.baiyi.opscloud.aliyun.rds.mysql;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/28 5:33 下午
 * @Version 1.0
 */
public interface AliyunRDSMysql {

    /**
     * 查询RDS-Mysql实例Map key=uid
     *
     * @return
     */
    Map<String, List<DescribeDBInstancesResponse.DBInstance>> getDBInstanceMap();

    Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>>
    getDBInstanceAttributeMap(Map<String, List<DescribeDBInstancesResponse.DBInstance>> dbInstanceMap);

    List<DescribeDatabasesResponse.Database> getDatabaseList(AliyunAccount aliyunAccount, String dbInstanceId);

    BusinessWrapper<Boolean> createAccount(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege);

    /**
     * 全库授权
     * @param aliyunAccount
     * @param ocCloudDbAccount
     * @param privilege
     * @return
     */
    BusinessWrapper<Boolean> grantAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege);

    /**
     * 全库撤销授权
     * @param aliyunAccount
     * @param ocCloudDbAccount
     * @return
     */
    BusinessWrapper<Boolean> revokeAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount);

    BusinessWrapper<Boolean> deleteAccount(AliyunAccount aliyunAccount, OcCloudDb ocCloudDb,  OcCloudDbAccount ocCloudDbAccount);
}
