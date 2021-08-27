package com.baiyi.opscloud.facade.datasource.impl;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.datasource.DsAssetSubscriptionParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.facade.datasource.DsInstanceAssetSubscriptionFacade;
import com.baiyi.opscloud.packer.datasource.DsAssetSubscriptionPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetSubscriptionService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:05 下午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetSubscriptionFacadeImpl implements DsInstanceAssetSubscriptionFacade {

    @Resource
    private DsAssetSubscriptionPacker dsAssetSubscriptionPacker;

    @Resource
    private DsInstanceAssetSubscriptionService dsInstanceAssetSubscriptionService;

    @Override
    public DataTable<DsAssetSubscriptionVO.AssetSubscription> queryAssetSubscriptionPage(DsAssetSubscriptionParam.AssetSubscriptionPageQuery pageQuery) {
        DataTable<DatasourceInstanceAssetSubscription> table = dsInstanceAssetSubscriptionService.queryPageByParam(pageQuery);
        return new DataTable<>(
                table.getData().stream().map(e->dsAssetSubscriptionPacker.wrapToVO(e,pageQuery)).collect(Collectors.toList()),
                table.getTotalNum());
    }


}
