package com.baiyi.opscloud.datasource.asset.impl;

import com.baiyi.opscloud.datasource.asset.impl.base.BaseAssetToBO;
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
 * @Date 2021/8/4 11:09 上午
 * @Version 1.0
 */
@Component
public class ZabbixHostAssetToServer extends BaseAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeEnum.ZABBIX_HOST.getType();
    }

    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        return ServerVO.Server.builder()
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                //.osType(captureName(asset.getProperties().get("osType"))) //首字大写
                //.area(asset.getZone())
                .assetId(asset.getId()) // 资产id
                .build();
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }

}
