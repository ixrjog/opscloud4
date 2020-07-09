package com.baiyi.opscloud.decorator.profile;

import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:15 上午
 * @Version 1.0
 */
@Component
public class ProfileSubscriptionDecorator {

    public ProfileSubscriptionVO.ProfileSubscription decorator(ProfileSubscriptionVO.ProfileSubscription profileSubscription){
        return profileSubscription;
    }
}
