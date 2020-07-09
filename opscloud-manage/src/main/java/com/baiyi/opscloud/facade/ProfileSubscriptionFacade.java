package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:04 上午
 * @Version 1.0
 */
public interface ProfileSubscriptionFacade {

    DataTable<ProfileSubscriptionVO.ProfileSubscription> queryProfileSubscriptionPage(ProfileSubscriptionParam.PageQuery pageQuery);
}
