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
 * @Date 2021/8/4 11:09 上午
 * @Version 1.0
 */
@Component
public class ZabbixHostAssetToServer extends AbstractAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.ZABBIX_HOST.name();
    }

    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return ServerVO.Server.builder()
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                //.osType(captureName(asset.getProperties().get("osType"))) //首字大写
                //.area(asset.getZone())
                // 资产ID
                .assetId(asset.getId())
                .build();
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }

}