package com.baiyi.opscloud.packer.user.child;

import com.baiyi.opscloud.common.datasource.AliyunDsInstanceConfig;
import com.baiyi.opscloud.core.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.SimpleExtend;
import com.baiyi.opscloud.domain.param.SimpleRelation;
import com.baiyi.opscloud.domain.types.DsAssetTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.packer.datasource.DsAssetPacker;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/9/8 9:25 上午
 * @Version 1.0
 */
@Component
public class RamUserPacker {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsAssetPacker dsAssetPacker;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    public void wrap(UserVO.User user) {
        DatasourceInstanceAsset param = DatasourceInstanceAsset.builder()
                .assetType(DsAssetTypeEnum.RAM_USER.name())
                .assetKey(user.getUsername())
                .isActive(true)
                .build();

        List<DatasourceInstanceAsset> data = dsInstanceAssetService.queryAssetByAssetParam(param);
        if (CollectionUtils.isEmpty(data)) return;
        user.setRamUsers(
                dsAssetPacker.wrapVOList(data, SimpleExtend.EXTEND, SimpleRelation.RELATION).stream().map(this::toRamUser).collect(Collectors.toList())
        );
    }

    private UserVO.RamUser toRamUser(DsAssetVO.Asset asset) {
        DatasourceInstance instance = dsInstanceService.getByUuid(asset.getInstanceUuid());
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getConfigId());
        AliyunDsInstanceConfig config = dsFactory.build(datasourceConfig, AliyunDsInstanceConfig.class);
        config.getAliyun().getAccount().getRamLoginUrl();
        List<DsAssetVO.Asset> accessKeys
                = asset.getTree().containsKey(DsAssetTypeEnum.RAM_ACCESS_KEY.name()) ? asset.getTree().get(DsAssetTypeEnum.RAM_ACCESS_KEY.name()) : Lists.newArrayList();
        List<DsAssetVO.Asset> ramPolicies
                = asset.getChildren().containsKey(DsAssetTypeEnum.RAM_POLICY.name()) ? asset.getChildren().get(DsAssetTypeEnum.RAM_POLICY.name()) : Lists.newArrayList();
        return UserVO.RamUser.builder()
                .username(asset.getAssetKey())
                .loginUser(asset.getAssetKey() + config.getAliyun().getAccount().getDomain())
                .loginUrl(config.getAliyun().getAccount().getRamLoginUrl())
                .instanceName(instance.getInstanceName())
                .name(asset.getName())
                .accessKeys(accessKeys)
                .ramPolicies(ramPolicies)
                .build();
    }
}
