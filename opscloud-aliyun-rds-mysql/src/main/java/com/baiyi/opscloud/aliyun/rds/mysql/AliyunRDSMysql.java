package com.baiyi.opscloud.aliyun.rds.mysql;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/2/28 5:33 下午
 * @Version 1.0
 */
public interface AliyunRDSMysql {

    Map<String, List<DescribeDBInstancesResponse.DBInstance>> getDBInstanceMap();

    Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>>
    getDBInstanceAttributeMap(Map<String, List<DescribeDBInstancesResponse.DBInstance>> dbInstanceMap);
}
