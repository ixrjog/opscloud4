package com.baiyi.opscloud.facade.impl;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.decorator.profile.ProfileSubscriptionDecorator;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcProfileSubscription;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.domain.vo.profile.ProfileSubscriptionVO;
import com.baiyi.opscloud.facade.ProfileSubscriptionFacade;
import com.baiyi.opscloud.service.profile.OcProfileSubscriptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:04 上午
 * @Version 1.0
 */
@Service
public class ProfileSubscriptionFacadeImpl implements ProfileSubscriptionFacade {

    @Resource
    private OcProfileSubscriptionService ocProfileSubscriptionService;

    @Resource
    private ProfileSubscriptionDecorator profileSubscriptionDecorator;

    @Override
    public DataTable<ProfileSubscriptionVO.ProfileSubscription> queryProfileSubscriptionPage(ProfileSubscriptionParam.PageQuery pageQuery) {
        DataTable<OcProfileSubscription> table = ocProfileSubscriptionService.queryOcProfileSubscriptionParam(pageQuery);
        List<ProfileSubscriptionVO.ProfileSubscription> page = BeanCopierUtils.copyListProperties(table.getData(), ProfileSubscriptionVO.ProfileSubscription.class);
        return new DataTable<>(page.stream().map(e -> profileSubscriptionDecorator.decorator(e)).collect(Collectors.toList()), table.getTotalNum());
    }

}
