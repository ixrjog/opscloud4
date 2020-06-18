package com.baiyi.opscloud.cloud.db;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbAccount;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;

/**
 * @Author baiyi
 * @Date 2020/2/28 7:05 下午
 * @Version 1.0
 */
public interface ICloudDB {

    /**
     * 同步云数据库实例
     *
     * @return
     */
    Boolean syncDBInstance();

    String getKey();

    /**
     * 同步数据库信息
     *
     * @param cloudDbId
     * @return
     */
    Boolean syncDatabase(int cloudDbId);

    /**
     * 创建账户并授权
     * @param ocCloudDbAccount
     * @param privilege
     * @return
     */
    BusinessWrapper<Boolean> createAccount(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount, String privilege);

    BusinessWrapper<Boolean> revokeAccountPrivilege(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount);

    BusinessWrapper<Boolean> deleteAccount(OcCloudDb ocCloudDb, OcCloudDbAccount ocCloudDbAccount);

    DataTable<CloudDatabaseSlowLogVO.SlowLog> querySlowLogPage(OcCloudDb ocCloudDb, CloudDBDatabaseParam.SlowLogPageQuery pageQuery);

}
