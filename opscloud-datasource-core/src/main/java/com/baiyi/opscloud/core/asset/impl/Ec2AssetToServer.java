package com.baiyi.opscloud.core.asset.impl;

import com.baiyi.opscloud.common.constants.OsTypeConstants;
import com.baiyi.opscloud.core.asset.impl.base.AbstractAssetToBO;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2022/1/25 4:22 PM
 * @Version 1.0
 */
@Component
public class Ec2AssetToServer extends AbstractAssetToBO {

    @Override
    public String getAssetType() {
        return DsAssetTypeConstants.EC2.name();
    }

    @Override
    protected BusinessAssetRelationVO.IBusinessAssetRelation toBO(DsAssetVO.Asset asset, BusinessTypeEnum businessTypeEnum) {
        String platform = asset.getProperties().get("platformDetails");
        Optional<OsTypeConstants> osTypeOptional = Arrays.stream(OsTypeConstants.values())
                .filter(t -> platform.startsWith(t.getDesc())
                        || platform.endsWith(t.getDesc())
                )
                .findFirst();
        return ServerVO.Server.builder()
                .name(asset.getName())
                .privateIp(asset.getAssetKey())
                .publicIp(asset.getAssetKey2())
                .envType(getDefaultEnvType())
                .osType(osTypeOptional.map(OsTypeConstants::getDesc).orElse(StringUtils.EMPTY))
                .area(asset.getZone())
                .build();
    }

    @Override
    public List<BusinessTypeEnum> getBusinessTypes() {
        return Lists.newArrayList(BusinessTypeEnum.SERVER);
    }

}