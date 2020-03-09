package com.baiyi.opscloud.aliyun.rds.mysql.impl;

import com.aliyuncs.rds.model.v20140815.DescribeAccountsResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunAccount;
import com.baiyi.opscloud.aliyun.rds.mysql.AliyunRDSMysql;
import com.baiyi.opscloud.aliyun.rds.mysql.handler.AliyunRDSMysqlHandler;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
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
        List<DescribeDatabasesResponse.Database> result = Lists.newArrayList();
        for (DescribeDatabasesResponse.Database database : databaseList) {
            if (database.getDBName().indexOf("__") == 0) continue; // 过滤内部数据库
            result.add(database);
        }

        return result;
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
                if (i + DB_INSTANCE_ID_MAX > listSize) {
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

    @Override
    public BusinessWrapper<Boolean> createAccount(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege) {
        BusinessWrapper<Boolean> wrapper = aliyunRDSMysqlHandler.createAccount(aliyunAccount, ocCloudDbAccount);
        // 查询账户详情
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);
        if (dbInstanceAccount == null)
            return wrapper;
        // 全库授权
        return grantAccountPrivilege(aliyunAccount, ocCloudDbAccount, privilege);
    }

    @Override
    public BusinessWrapper<Boolean> grantAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege) {
        List<DescribeDatabasesResponse.Database> databaseList = getDatabaseList(aliyunAccount, ocCloudDbAccount.getDbInstanceId());
        if (databaseList.isEmpty())
            return BusinessWrapper.SUCCESS;
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);

        Map<String, DescribeAccountsResponse.DBInstanceAccount.DatabasePrivilege> databasePrivilegeMap =
                dbInstanceAccount.getDatabasePrivileges().stream().collect(Collectors.toMap(DescribeAccountsResponse.DBInstanceAccount.DatabasePrivilege::getDBName, a -> a, (k1, k2) -> k1));

        if (dbInstanceAccount == null)
            return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_DESCRIBE_ACCOUNT_ERROR);
        for (DescribeDatabasesResponse.Database database : databaseList) {
            if (databasePrivilegeMap.containsKey(database.getDBName())) continue; // 已授权
            BusinessWrapper<Boolean> wrapper = aliyunRDSMysqlHandler.grantAccountPrivilege(aliyunAccount, ocCloudDbAccount, database.getDBName());
            if (!wrapper.isSuccess()) return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> revokeAccountPrivilege(AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount) {
        List<DescribeDatabasesResponse.Database> databaseList = getDatabaseList(aliyunAccount, ocCloudDbAccount.getDbInstanceId());
        if (databaseList.isEmpty())
            return BusinessWrapper.SUCCESS;
        for (DescribeDatabasesResponse.Database database : databaseList)
            aliyunRDSMysqlHandler.revokeAccountPrivilege(aliyunAccount, ocCloudDbAccount, database.getDBName());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteAccount(AliyunAccount aliyunAccount, OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount) {
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);
        if (dbInstanceAccount != null)
            aliyunRDSMysqlHandler.deleteAccount(aliyunAccount, ocCloudDb.getDbInstanceId(), ocCloudDbAccount.getAccountName());
        return BusinessWrapper.SUCCESS;
    }

}
