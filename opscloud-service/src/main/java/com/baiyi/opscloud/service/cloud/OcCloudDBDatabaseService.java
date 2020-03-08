package com.baiyi.opscloud.service.cloud;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudDbDatabase;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/1 6:52 下午
 * @Version 1.0
 */
public interface OcCloudDBDatabaseService {

    OcCloudDbDatabase queryOcCloudDbDatabaseById(int id);

    OcCloudDbDatabase queryOcCloudDbDatabaseByUniqueKey(int cloudDbId, String dbName);

    void addOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase);

    void updateOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase);

    void delOcCloudDbDatabaseById(int id);

    List<OcCloudDbDatabase> queryOcCloudDbDatabaseByCloudDbId(int cloudDbId);

    DataTable<OcCloudDbDatabase> fuzzyQueryOcCloudDBDatabaseByParam(CloudDBDatabaseParam.PageQuery pageQuery);

    void updateBaseOcCloudDbDatabase(OcCloudDbDatabase ocCloudDbDatabase);
}
