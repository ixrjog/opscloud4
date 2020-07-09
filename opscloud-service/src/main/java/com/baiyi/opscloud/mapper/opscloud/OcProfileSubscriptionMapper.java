package com.baiyi.opscloud.mapper.opscloud;

import com.baiyi.opscloud.domain.generator.opscloud.OcProfileSubscription;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface OcProfileSubscriptionMapper extends Mapper<OcProfileSubscription> {

    List<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery);
}