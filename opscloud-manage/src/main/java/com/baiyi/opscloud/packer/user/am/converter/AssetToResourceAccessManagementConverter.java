package com.baiyi.opscloud.packer.user.am.converter;

import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.AccessManagementVO;
import com.google.common.base.Joiner;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:47 PM
 * @Version 1.0
 */
@Component
public class AssetToResourceAccessManagementConverter extends AbstractAssetToAccessManagementConverter {

    @Override
    protected void wrap(AccessManagementVO.XAccessManagement xam, DatasourceConfig datasourceConfig) {
        AliyunConfig config = dsConfigManager.build(datasourceConfig, AliyunConfig.class);
        xam.setLoginUser(Joiner.on("").join(xam.getUsername(), config.getAliyun().getAccount().getDomain()));
        xam.setLoginUrl(config.getAliyun().getAccount().getLoginUrl(config.getAliyun().getVersion()));
    }

    protected List<DsAssetVO.Asset> toAccessKeys(DsAssetVO.Asset asset) {
        return asset.getTree().containsKey(DsAssetTypeConstants.RAM_ACCESS_KEY.name()) ? asset.getTree().get(DsAssetTypeConstants.RAM_ACCESS_KEY.name()) : Lists.newArrayList();
    }

    protected List<DsAssetVO.Asset> toPolicies(DsAssetVO.Asset asset) {
        return asset.getChildren().containsKey(DsAssetTypeConstants.RAM_POLICY.name()) ? asset.getChildren().get(DsAssetTypeConstants.RAM_POLICY.name()) : Lists.newArrayList();
    }

    @Override
    public String getAMType() {
        return DsAssetTypeConstants.RAM_USER.name();
    }

}
