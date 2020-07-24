package com.baiyi.opscloud.aliyun.rds.mysql.impl;

import com.aliyuncs.rds.model.v20140815.*;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.rds.mysql.AliyunRDSMysql;
import com.baiyi.opscloud.aliyun.rds.mysql.handler.AliyunRDSMysqlHandler;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
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
    public List<DescribeDatabasesResponse.Database> getDatabaseList(AliyunCoreConfig.AliyunAccount aliyunAccount, String dbInstanceId) {
        return aliyunRDSMysqlHandler.getDatabaseList(aliyunAccount, dbInstanceId)
                .stream().filter(e -> e.getDBName().indexOf("__") != 0).collect(Collectors.toList());
    }

    @Override
    public Map<String, List<DescribeDBInstancesResponse.DBInstance>> getDBInstanceMap() {
        List<AliyunCoreConfig.AliyunAccount> accounts = aliyunCore.getAccounts();
        Map<String, List<DescribeDBInstancesResponse.DBInstance>> instanceMap = Maps.newHashMap();
        accounts.forEach(e -> {
            // 遍历所有可用区
            List<DescribeDBInstancesResponse.DBInstance> dbInstanceList = Lists.newArrayList();
            for (String regionId : e.getRegionIds())
                dbInstanceList.addAll(aliyunRDSMysqlHandler.getDbInstanceList(regionId, e));
            instanceMap.put(e.getUid(), dbInstanceList);
        });
        return instanceMap;
    }

    @Override
    public Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>> getDBInstanceAttributeMap(Map<String, List<DescribeDBInstancesResponse.DBInstance>> dbInstanceMap) {
        Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>> dbInstanceAttributeMap = Maps.newHashMap();
        for (String uid : dbInstanceMap.keySet()) {
            AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(uid);
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
                String dbInstanceIds = Joiner.on(",").join(subList.stream().map(DescribeDBInstancesResponse.DBInstance::getDBInstanceId).collect(Collectors.toList()));
                dbInstanceAttributeList.addAll(aliyunRDSMysqlHandler.getDbInstanceAttribute(aliyunAccount, dbInstanceIds));
            }
            dbInstanceAttributeMap.put(uid, dbInstanceAttributeList);
        }
        return dbInstanceAttributeMap;
    }

    @Override
    public BusinessWrapper<Boolean> createAccount(AliyunCoreConfig.AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege) {
        BusinessWrapper<Boolean> wrapper = aliyunRDSMysqlHandler.createAccount(aliyunAccount, ocCloudDbAccount);
        // 查询账户详情
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);
        if (dbInstanceAccount == null)
            return wrapper;
        // 全库授权
        return grantAccountPrivilege(aliyunAccount, ocCloudDbAccount, privilege);
    }

    @Override
    public BusinessWrapper<Boolean> grantAccountPrivilege(AliyunCoreConfig.AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount, String privilege) {
        List<DescribeDatabasesResponse.Database> databaseList = getDatabaseList(aliyunAccount, ocCloudDbAccount.getDbInstanceId());
        if (databaseList.isEmpty())
            return BusinessWrapper.SUCCESS;
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);
        if (dbInstanceAccount == null)
            return new BusinessWrapper(ErrorEnum.ALIYUN_RDS_MYSQL_DESCRIBE_ACCOUNT_ERROR);
        Map<String, DescribeAccountsResponse.DBInstanceAccount.DatabasePrivilege> databasePrivilegeMap =
                dbInstanceAccount.getDatabasePrivileges().stream().collect(Collectors.toMap(DescribeAccountsResponse.DBInstanceAccount.DatabasePrivilege::getDBName, a -> a, (k1, k2) -> k1));
        for (DescribeDatabasesResponse.Database database : databaseList) {
            if (databasePrivilegeMap.containsKey(database.getDBName())) continue; // 已授权
            BusinessWrapper<Boolean> wrapper = aliyunRDSMysqlHandler.grantAccountPrivilege(aliyunAccount, ocCloudDbAccount, database.getDBName());
            if (!wrapper.isSuccess()) return wrapper;
        }
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> revokeAccountPrivilege(AliyunCoreConfig.AliyunAccount aliyunAccount, OcCloudDbAccount ocCloudDbAccount) {
        List<DescribeDatabasesResponse.Database> databaseList = getDatabaseList(aliyunAccount, ocCloudDbAccount.getDbInstanceId());
        if (databaseList.isEmpty())
            return BusinessWrapper.SUCCESS;
        for (DescribeDatabasesResponse.Database database : databaseList)
            aliyunRDSMysqlHandler.revokeAccountPrivilege(aliyunAccount, ocCloudDbAccount, database.getDBName());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public BusinessWrapper<Boolean> deleteAccount(AliyunCoreConfig.AliyunAccount aliyunAccount, OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount) {
        DescribeAccountsResponse.DBInstanceAccount dbInstanceAccount = aliyunRDSMysqlHandler.getAccount(aliyunAccount, ocCloudDbAccount);
        if (dbInstanceAccount != null)
            aliyunRDSMysqlHandler.deleteAccount(aliyunAccount, ocCloudDb.getDbInstanceId(), ocCloudDbAccount.getAccountName());
        return BusinessWrapper.SUCCESS;
    }

    @Override
    public DataTable<CloudDatabaseSlowLogVO.SlowLog> querySlowLogPage(AliyunCoreConfig.AliyunAccount aliyunAccount, CloudDBDatabaseParam.SlowLogPageQuery pageQuery) {
        DescribeSlowLogsRequest request = new DescribeSlowLogsRequest();
        request.setDBInstanceId(pageQuery.getDbInstanceId());
        request.setDBName(pageQuery.getDbName());
        request.setStartTime(pageQuery.getStartTime());
        request.setEndTime(pageQuery.getEndTime());
        request.setPageSize(pageQuery.getLength() < 30 ? 30 : pageQuery.getLength());
        request.setPageNumber(pageQuery.getPage());
        DescribeSlowLogsResponse response = aliyunRDSMysqlHandler.describeDBInstancesResponse(request, aliyunAccount);
        if (response != null) {
            List<CloudDatabaseSlowLogVO.SlowLog> page = BeanCopierUtils.copyListProperties(response.getItems(), CloudDatabaseSlowLogVO.SlowLog.class);
            return new DataTable<>(page, response.getTotalRecordCount());
        }
        return new DataTable<>(Lists.newArrayList(), 0);
    }

}
