package com.baiyi.opscloud.facade;

import com.baiyi.opscloud.domain.BusinessWrapper;
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

    BusinessWrapper<Boolean> addProfileSubscription(ProfileSubscriptionVO.ProfileSubscription profileSubscription);

    BusinessWrapper<Boolean> updateProfileSubscription(ProfileSubscriptionVO.ProfileSubscription profileSubscription);

    /**
     * 发布配置文件
     * @param id
     * @return
     */
    void publishProfile(int id);

    /**
     * 按订阅类型发布配置文件
     * @param subscriptionType
     * @return
     */
    void publishProfile(String subscriptionType);
}
