package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.cloud.CloudDBDatabaseParam;
import com.baiyi.opscloud.domain.param.cloud.CloudDBParam;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBAccountVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBDatabaseVO;
import com.baiyi.opscloud.domain.vo.cloud.OcCloudDBVO;

/**
 * @Author baiyi
 * @Date 2020/3/1 12:20 下午
 * @Version 1.0
 */
public interface CloudDBFacade {

    DataTable<OcCloudDBVO.CloudDB> fuzzyQueryCloudDBPage(CloudDBParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> deleteCloudDBById(int id);

    BusinessWrapper<Boolean> syncCloudDB();

    BusinessWrapper<Boolean> syncCloudDatabase(int id);

    DataTable<OcCloudDBDatabaseVO.CloudDBDatabase> fuzzyQueryCloudDBDatabasePage(CloudDBDatabaseParam.PageQuery pageQuery);

    BusinessWrapper<Boolean> updateBaseCloudDBDatabase(OcCloudDBDatabaseVO.CloudDBDatabase cloudDBDatabase);

    BusinessWrapper<Boolean> privilegeAccount(OcCloudDBAccountVO.PrivilegeAccount privilegeAccount);

}
