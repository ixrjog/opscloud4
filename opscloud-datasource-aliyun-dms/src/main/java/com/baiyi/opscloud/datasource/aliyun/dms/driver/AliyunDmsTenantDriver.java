package com.baiyi.opscloud.datasource.aliyun.dms.driver;

import com.aliyun.dms_enterprise20181101.models.GetUserActiveTenantRequest;
import com.aliyun.dms_enterprise20181101.models.GetUserActiveTenantResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.aliyun.dms.client.AliyunDmsClient;
import com.baiyi.opscloud.datasource.aliyun.dms.entity.DmsTenant;

/**
 * @Author baiyi
 * @Date 2021/12/16 5:22 PM
 * @Version 1.0
 */
public class AliyunDmsTenantDriver {

    public static DmsTenant.Tenant getTenant(AliyunConfig.Aliyun aliyun) throws Exception {
        com.aliyun.dms_enterprise20181101.Client client = AliyunDmsClient.createClient(aliyun);
        GetUserActiveTenantRequest request = new GetUserActiveTenantRequest();
        GetUserActiveTenantResponse response = client.getUserActiveTenant(request);
        response.getBody().getTenant();
        return BeanCopierUtil.copyProperties(response.getBody().getTenant(), DmsTenant.Tenant.class);
    }

}