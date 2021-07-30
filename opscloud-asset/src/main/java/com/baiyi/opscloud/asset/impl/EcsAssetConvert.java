package com.baiyi.opscloud.asset.impl;

import com.baiyi.opscloud.asset.impl.base.BaseAssetConvert;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
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
public class EcsAssetConvert extends BaseAssetConvert<ServerVO.Server> {

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ECS.getType();
    }

    protected ServerVO.Server toBusinessObject(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        ServerVO.Server server = ServerVO.Server.builder()
                .id(0)
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                .osType(captureName(asset.getProperties().get("osType").toString()))
                .area(asset.getZone())
                .serverType(0)
                .isActive(true)
                .serialNumber(0)
                .datasourceInstanceAssetId(asset.getId()) //建立关系
                .build();
        return server;
    }


    @Override
    protected List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }


}
