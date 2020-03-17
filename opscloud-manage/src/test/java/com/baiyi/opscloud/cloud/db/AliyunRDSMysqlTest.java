package com.baiyi.opscloud.cloud.db;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.db.factory.CloudDBFactory;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:20 下午
 * @Version 1.0
 */
public class AliyunRDSMysqlTest extends BaseUnit {

    private static final String key = "AliyunRDSMysqlCloudDB";

    private ICloudDB getICloudDB() {
        return CloudDBFactory.getCloudDBByKey(key);
    }

    @Test
    void testSync() {
       getICloudDB().syncDBInstance();
    }


    @Test
    void testDBName() {
        String dbName = "__recycle_bin__";
        System.err.println(dbName.indexOf("__"));
    }



}
