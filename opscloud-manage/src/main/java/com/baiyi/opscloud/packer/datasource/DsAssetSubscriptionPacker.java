package com.baiyi.opscloud.packer.datasource;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetSubscription;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.common.util.time.AgoUtil;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:19 下午
 * @Version 1.0
 */
@Component
public class DsAssetSubscriptionPacker {

    @Resource
    private DsInstancePacker dsInstancePacker;

    @Resource
    private DsAssetPacker dsAssetPacker;

    public DsAssetSubscriptionVO.AssetSubscription wrapToVO(DatasourceInstanceAssetSubscription assetSubscription, IExtend iExtend) {
        DsAssetSubscriptionVO.AssetSubscription vo = BeanCopierUtil.copyProperties(assetSubscription, DsAssetSubscriptionVO.AssetSubscription.class);
        if (iExtend.getExtend()) {
            dsInstancePacker.wrap(vo);
            dsAssetPacker.wrap(vo);
            AgoUtil.wrap(vo);
        }
        return vo;
    }
}
