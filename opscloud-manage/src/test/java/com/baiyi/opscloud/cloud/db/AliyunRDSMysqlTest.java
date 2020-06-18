package com.baiyi.opscloud.cloud.db;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.cloud.db.factory.CloudDBFactory;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDb;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
import com.baiyi.opscloud.service.cloud.OcCloudDBService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/1/10 11:20 下午
 * @Version 1.0
 */
public class AliyunRDSMysqlTest extends BaseUnit {

    private static final String key = "AliyunRDSMysqlCloudDB";

    @Resource
    private OcCloudDBService ocCloudDBService;

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


    @Test
    void testPage() {
        CloudDBDatabaseParam.SlowLogPageQuery pageQuery = new  CloudDBDatabaseParam.SlowLogPageQuery();
        pageQuery.setCloudDbType(2);
        pageQuery.setDbInstanceId("rm-bp15in4n827sceu6j");
        pageQuery.setDbName("opscloud");
        pageQuery.setPage(1);
        pageQuery.setLength(10);
        pageQuery.setStartTime("2020-03-21Z");
        pageQuery.setEndTime("2020-04-03Z");
        ICloudDB iCloudDB =   getICloudDB();
        OcCloudDb ocCloudDb = ocCloudDBService.queryOcCloudDbByUniqueKey(pageQuery.getCloudDbType(), pageQuery.getDbInstanceId());
        DataTable<CloudDatabaseSlowLogVO.SlowLog>  table =iCloudDB.querySlowLogPage(ocCloudDb,pageQuery);
        System.err.println(JSON.toJSONString(table));
    }


}
