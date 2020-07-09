package com.baiyi.opscloud.service.profile;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcProfileSubscription;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:05 上午
 * @Version 1.0
 */
public interface OcProfileSubscriptionService {

    DataTable<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery);
}
