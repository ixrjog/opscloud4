package com.baiyi.opscloud.packer.user.am.converter;

import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AMVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:47 PM
 * @Version 1.0
 */
@Component
public class AssetToIAMConverter extends AbstractToAMConverter {

    @Override
    public AMVO.XAM toAM(DsAssetVO.Asset asset) {
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getConfigId());
        AwsConfig config = dsConfigHelper.build(datasourceConfig, AwsConfig.class);
        List<DsAssetVO.Asset> accessKeys
                = asset.getTree().containsKey(DsAssetTypeConstants.IAM_ACCESS_KEY.name()) ? asset.getTree().get(DsAssetTypeConstants.IAM_ACCESS_KEY.name()) : Lists.newArrayList();
        List<DsAssetVO.Asset> ramPolicies
                = asset.getChildren().containsKey(DsAssetTypeConstants.IAM_POLICY.name()) ? asset.getChildren().get(DsAssetTypeConstants.IAM_POLICY.name()) : Lists.newArrayList();
        return AMVO.IAM.builder()
                .instanceUuid(instance.getUuid())
                .instanceName(instance.getInstanceName())
                .username(asset.getAssetKey())
                .loginUser(asset.getAssetKey())
                .loginUrl(config.getAws().getAccount().getLoginUrl())
                .name(asset.getName())
                .accessKeys(accessKeys)
                .policies(ramPolicies)
                .build();
    }

    @Override
    public String getAMType() {
        return DsAssetTypeConstants.IAM_USER.name();
    }
}
