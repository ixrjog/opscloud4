package com.baiyi.opscloud.aliyun.rds.mysql;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;

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
}
