package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.domain.vo.cloud.CloudDatabaseSlowLogVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.CloudDBVO;

/**
 * @Author baiyi
 * @Date 2020/3/1 12:20 下午
 * @Version 1.0
 */
public interface CloudDBFacade {

    DataTable<CloudDBVO.CloudDB> fuzzyQueryCloudDBPage(CloudDBParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudDBById(int id);

    BusinessWrapper<Boolean> syncCloudDB();

    BusinessWrapper<Boolean> syncCloudDatabase(int id);

    DataTable<CloudDBDatabaseVO.CloudDBDatabase> fuzzyQueryCloudDBDatabasePage(CloudDBDatabaseParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> updateBaseCloudDBDatabase(CloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase);

    BusinessWrapper<Boolean> privilegeAccount(CloudDBAccountVO.PrivilegeAccount privilegeAccount);

    DataTable<CloudDatabaseSlowLogVO.SlowLog> queryCloudDBDatabaseSlowLogPage(CloudDBDatabaseParam.SlowLogPageQuery pageQuery);

}
