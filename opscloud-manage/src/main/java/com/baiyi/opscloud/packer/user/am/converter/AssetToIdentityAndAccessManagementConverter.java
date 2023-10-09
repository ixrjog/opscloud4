package com.baiyi.opscloud.packer.user.am.converter;

import com.baiyi.opscloud.common.datasource.AwsConfig;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:47 PM
 * @Version 1.0
 */
@Component
public class AssetToIdentityAndAccessManagementConverter extends AbstractAssetToAccessManagementConverter {

    @Override
    protected void wrap(AccessManagementVO.XAccessManagement xam, DatasourceConfig datasourceConfig) {
        AwsConfig config = dsConfigManager.build(datasourceConfig, AwsConfig.class);
        xam.setLoginUser(xam.getUsername());
        xam.setLoginUrl(config.getAws().getAccount().getLoginUrl());
    }

    protected List<DsAssetVO.Asset> toAccessKeys(DsAssetVO.Asset asset) {
        return asset.getTree().containsKey(DsAssetTypeConstants.IAM_ACCESS_KEY.name()) ? asset.getTree().get(DsAssetTypeConstants.IAM_ACCESS_KEY.name()) : Lists.newArrayList();
    }

    protected List<DsAssetVO.Asset> toPolicies(DsAssetVO.Asset asset) {
        return asset.getChildren().containsKey(DsAssetTypeConstants.IAM_POLICY.name()) ? asset.getChildren().get(DsAssetTypeConstants.IAM_POLICY.name()) : Lists.newArrayList();
    }

    @Override
    public String getAMType() {
        return DsAssetTypeConstants.IAM_USER.name();
    }
}
