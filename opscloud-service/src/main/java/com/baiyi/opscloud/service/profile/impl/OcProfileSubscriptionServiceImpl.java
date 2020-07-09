package com.baiyi.opscloud.service.profile.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.OcProfileSubscription;
import com.baiyi.opscloud.domain.param.profile.ProfileSubscriptionParam;
import com.baiyi.opscloud.mapper.opscloud.OcProfileSubscriptionMapper;
import com.baiyi.opscloud.service.profile.OcProfileSubscriptionService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/7/9 9:06 上午
 * @Version 1.0
 */
@Service
public class OcProfileSubscriptionServiceImpl implements OcProfileSubscriptionService {

    @Resource
    private OcProfileSubscriptionMapper ocProfileSubscriptionMapper;

    @Override
    public DataTable<OcProfileSubscription> queryOcProfileSubscriptionParam(ProfileSubscriptionParam.PageQuery pageQuery) {
        Page page = PageHelper.startPage(pageQuery.getPage(), pageQuery.getLength().intValue());
        List<OcProfileSubscription> list = ocProfileSubscriptionMapper.queryOcProfileSubscriptionParam(pageQuery);
        return new DataTable<>(list, page.getTotal());
    }

}
