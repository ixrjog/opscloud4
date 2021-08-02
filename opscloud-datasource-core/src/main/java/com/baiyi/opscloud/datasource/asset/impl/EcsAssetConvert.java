package com.baiyi.opscloud.datasource.asset.impl;

import com.baiyi.opscloud.datasource.asset.impl.base.BaseAssetConvert;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/30 1:25 下午
 * @Version 1.0
 */
@Component
public class EcsAssetConvert extends BaseAssetConvert {

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ECS.getType();
    }

    protected BusinessAssetRelationVO.IBusinessAssetRelation toBusinessObject(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return ServerVO.Server.builder()
                .id(0)
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                .osType(captureName(asset.getProperties().get("osType"))) //首字大写
                .area(asset.getZone())
                .assetId(asset.getId()) // 资产id
                .build();
    }

    @Override
    protected List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }

}
