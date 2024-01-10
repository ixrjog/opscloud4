package com.baiyi.opscloud.core.asset.impl;

import com.baiyi.opscloud.core.asset.impl.base.AbstractAssetToBO;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
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
public class EcsAssetToServer extends AbstractAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ECS.name();
    }

    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return ServerVO.Server.builder()
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                // 首字大写
                .osType(captureName(asset.getProperties().get("osType")))
                .area(asset.getZone())
                .build();
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }

}