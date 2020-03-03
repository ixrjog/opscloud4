package com.baiyi.opscloud.aliyun.rds.mysql.impl;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.aliyun.rds.mysql.AliyunRDSMysql;
import com.baiyi.opscloud.aliyun.rds.mysql.handler.AliyunRDSMysqlHandler;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/28 5:34 下午
 * @Version 1.0
 */
@Component("AliyunRDSMysql")
public class AliyunRDSMysqlImpl implements AliyunRDSMysql {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunRDSMysqlHandler aliyunRDSMysqlHandler;

    public static final int DB_INSTANCE_ID_MAX = 30; // API限制最大查询30个id

    @Override
    public List<DescribeDatabasesResponse.Database> getDatabaseList(AliyunAccount aliyunAccount, String dbInstanceId) {
        List<DescribeDatabasesResponse.Database> databaseList =
                aliyunRDSMysqlHandler.getDatabaseList(aliyunAccount, dbInstanceId);
        return databaseList;
    }


    @Override
    public Map<String, List<DescribeDBInstancesResponse.DBInstance>> getDBInstanceMap() {
        List<AliyunAccount> accounts = aliyunCore.getAccounts();
        Map<String, List<DescribeDBInstancesResponse.DBInstance>> instanceMap = Maps.newHashMap();
        for (AliyunAccount aliyunAccount : accounts) {
            // 遍历所有可用区
            List<DescribeDBInstancesResponse.DBInstance> dbInstanceList = Lists.newArrayList();
            for (String regionId : aliyunAccount.getRegionIds()) {
                dbInstanceList.addAll(aliyunRDSMysqlHandler.getDbInstanceList(regionId, aliyunAccount));
            }
            instanceMap.put(aliyunAccount.getUid(), dbInstanceList);
        }
        return instanceMap;
    }

    @Override
    public Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>> getDBInstanceAttributeMap(Map<String, List<DescribeDBInstancesResponse.DBInstance>> dbInstanceMap) {
        Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>> dbInstanceAttributeMap = Maps.newHashMap();
        for (String uid : dbInstanceMap.keySet()) {
            AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(uid);
            List<DescribeDBInstancesResponse.DBInstance> dbInstanceList = dbInstanceMap.get(uid);
            int listSize = dbInstanceList.size();
            int toIndex = DB_INSTANCE_ID_MAX;
            List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> dbInstanceAttributeList = Lists.newArrayList();
            for (int i = 0; i < dbInstanceList.size(); i += DB_INSTANCE_ID_MAX) {
                if (i + DB_INSTANCE_ID_MAX > listSize) {        //作用为toIndex最后没有100条数据则剩余几条newList中就装几条
                    toIndex = listSize - i;
                }
                List<DescribeDBInstancesResponse.DBInstance> subList = dbInstanceList.subList(i, i + toIndex);
                // 查询ids
                String dbInstanceIds = Joiner.on(",").join(subList.stream().map(e -> e.getDBInstanceId()).collect(Collectors.toList()));
                dbInstanceAttributeList.addAll(aliyunRDSMysqlHandler.getDbInstanceAttribute(aliyunAccount, dbInstanceIds));
            }
            dbInstanceAttributeMap.put(uid, dbInstanceAttributeList);
        }
        return dbInstanceAttributeMap;
    }


}
