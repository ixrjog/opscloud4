package com.baiyi.opscloud.cloud.db.impl;

import com.aliyuncs.rds.model.v20140815.DescribeDBInstanceAttributeResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDBInstancesResponse;
import com.aliyuncs.rds.model.v20140815.DescribeDatabasesResponse;
import com.baiyi.opscloud.aliyun.core.AliyunCore;
import com.baiyi.opscloud.aliyun.core.config.AliyunCoreConfig;
import com.baiyi.opscloud.aliyun.rds.mysql.AliyunRDSMysql;
import com.baiyi.opscloud.cloud.account.CloudAccount;
import com.baiyi.opscloud.cloud.db.ICloudDB;
import com.baiyi.opscloud.cloud.db.builder.CloudDbAttributeBuilder;
import com.baiyi.opscloud.cloud.db.builder.CloudDbBuilder;
import com.baiyi.opscloud.cloud.db.builder.CloudDbDatabaseBuilder;
import com.baiyi.opscloud.common.base.CloudDBType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAttribute;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:09 下午
 * @Version 1.0
 */
@Slf4j
@Component("AliyunRDSMysqlCloudDB")
public class AliyunRDSMysqlCloudDB<T> extends BaseCloudDB<T> implements ICloudDB {

    @Resource
    private AliyunCore aliyunCore;

    @Resource
    private AliyunRDSMysql aliyunRDSMysql;

    @Override
    protected Map<String, List<T>> getDBInstanceMap() {
        Map<String, List<T>> dbInstanceMap = Maps.newHashMap();
        Map<String, List<DescribeDBInstancesResponse.DBInstance>> map = aliyunRDSMysql.getDBInstanceMap();
        for (String uid : map.keySet())
            dbInstanceMap.put(uid, (List<T>) map.get(uid));
        return dbInstanceMap;
    }

    @Override
    protected Boolean syncDatabase(String uid, OcCloudDb ocCloudDb) {
        List<DescribeDatabasesResponse.Database> databaseList =
                aliyunRDSMysql.getDatabaseList(aliyunCore.getAliyunAccountByUid(uid), ocCloudDb.getDbInstanceId());
        List<OcCloudDbDatabase> ocCloudDbDatabaseList = getOcCloudDbDatabaseList(ocCloudDb.getId(), databaseList);
        return saveOcCloudDbDatabaseList(ocCloudDbDatabaseList);
    }

    private List<OcCloudDbDatabase> getOcCloudDbDatabaseList(int ocCloudDbId, List<DescribeDatabasesResponse.Database> databaseList) {
        return databaseList.stream().map(e -> CloudDbDatabaseBuilder.build(ocCloudDbId, e)).collect(Collectors.toList());
    }


    @Override
    protected void saveDBInstanceAttribute(Map<String, List<T>> dbInstanceMap) {
        Map<String, List<DescribeDBInstancesResponse.DBInstance>> map = Maps.newHashMap();
        for (String uid : dbInstanceMap.keySet())
            map.put(uid, (List<DescribeDBInstancesResponse.DBInstance>) dbInstanceMap.get(uid));
        Map<String, List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute>> dbInstanceAttributeMap = aliyunRDSMysql.getDBInstanceAttributeMap(map);
        for (String uid : dbInstanceAttributeMap.keySet()) {
            List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> dbInstanceAttributeList = dbInstanceAttributeMap.get(uid);
            saveDBInstanceAttributeList(dbInstanceAttributeList);
        }
    }

    private void saveDBInstanceAttributeList(List<DescribeDBInstanceAttributeResponse.DBInstanceAttribute> dbInstanceAttributeList) {
        for (DescribeDBInstanceAttributeResponse.DBInstanceAttribute attribute : dbInstanceAttributeList) {
            List<OcCloudDbAttribute> attributeList = getOcCloudDbAttribute(attribute);
            saveOcCloudDbAttributeList(attribute.getDBInstanceId(), attributeList);
        }
    }

    private List<OcCloudDbAttribute> getOcCloudDbAttribute(DescribeDBInstanceAttributeResponse.DBInstanceAttribute attribute) {
        List<OcCloudDbAttribute> attributeList = Lists.newArrayList();
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "DBInstanceClassType", attribute.getDBInstanceClassType()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "ConnectionString", attribute.getConnectionString()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "Port", attribute.getPort()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "DBInstanceMemory", attribute.getDBInstanceMemory()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "DBInstanceStorage", attribute.getDBInstanceStorage()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "DBMaxQuantity", attribute.getDBMaxQuantity()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "AccountMaxQuantity", attribute.getAccountMaxQuantity()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "AvailabilityValue", attribute.getAvailabilityValue()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "MaxIOPS", attribute.getMaxIOPS()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "MaxConnections", attribute.getMaxConnections()));
        attributeList.add(CloudDbAttributeBuilder.build(attribute.getDBInstanceId(), "DBInstanceStorageType", attribute.getDBInstanceStorageType()));
        return attributeList;
    }

    @Override
    protected CloudAccount getCloudAccountByUid(String uid) {
        AliyunCoreConfig.AliyunAccount account = aliyunCore.getAliyunAccountByUid(uid);
        return BeanCopierUtils.copyProperties(account, CloudAccount.class);
    }

    @Override
    protected String getDBInstanceId(T dbInstance) throws Exception {
        if (!(dbInstance instanceof DescribeDBInstancesResponse.DBInstance)) throw new Exception();
        DescribeDBInstancesResponse.DBInstance i = (DescribeDBInstancesResponse.DBInstance) dbInstance;
        return i.getDBInstanceId();
    }

    @Override
    protected OcCloudDb getOcCloudDb(CloudAccount cloudAccount, T dbInstance) throws Exception {
        if (!(dbInstance instanceof DescribeDBInstancesResponse.DBInstance)) throw new Exception();
        DescribeDBInstancesResponse.DBInstance i = (DescribeDBInstancesResponse.DBInstance) dbInstance;
        return CloudDbBuilder.build(cloudAccount, i);
    }

    @Override
    protected int getCloudDBType() {
        return CloudDBType.ALIYUN_RDS_MYSQL.getType();
    }

    @Override
    public BusinessWrapper<Boolean> createAccount(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount, String privilege) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ocCloudDb.getUid());
        return aliyunRDSMysql.createAccount(aliyunAccount, ocCloudDbAccount, privilege);
    }

    @Override
    public DataTable<CloudDatabaseSlowLogVO.SlowLog> querySlowLogPage(OcCloudDb ocCloudDb, CloudDBDatabaseParam.SlowLogPageQuery pageQuery) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ocCloudDb.getUid());
        return aliyunRDSMysql.querySlowLogPage(aliyunAccount, pageQuery);
    }


    @Override
    public BusinessWrapper<Boolean> deleteAccount(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ocCloudDb.getUid());
        return aliyunRDSMysql.deleteAccount(aliyunAccount, ocCloudDb, ocCloudDbAccount);
    }

    @Override
    public BusinessWrapper<Boolean> revokeAccountPrivilege(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount) {
        AliyunCoreConfig.AliyunAccount aliyunAccount = aliyunCore.getAliyunAccountByUid(ocCloudDb.getUid());
        return aliyunRDSMysql.revokeAccountPrivilege(aliyunAccount, ocCloudDbAccount);
    }

}
