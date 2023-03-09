package com.baiyi.opscloud.packer.datasource;

import com.baiyi.opscloud.common.annotation.AgoWrapper;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetSubscriptionVO;
import com.baiyi.opscloud.packer.IWrapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2021/8/27 4:19 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DsAssetSubscriptionPacker implements IWrapper<DsAssetSubscriptionVO.AssetSubscription> {

    private final DsInstancePacker dsInstancePacker;

    private final DsAssetPacker dsAssetPacker;

    @Override
    @AgoWrapper
    public void wrap(DsAssetSubscriptionVO.AssetSubscription assetSubscription, IExtend iExtend) {
        if (!iExtend.getExtend()) {
            return;
        }
        dsInstancePacker.wrap(assetSubscription);
        dsAssetPacker.wrap(assetSubscription);
    }

}
