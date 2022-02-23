package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.kubernetes.provider.KubernetesPodProvider;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/2/18 2:01 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationResourcePacker implements IWrapper<ApplicationResourceVO.Resource> {

    private final KubernetesPodProvider kubernetesPodProvider;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceService dsInstanceService;

    private final ApplicationResourceInstancePacker applicationResourceInstancePacker;

    /**
     * wrapPodByDeployment
     * @param resource
     */
    @Override
    public void wrap(ApplicationResourceVO.Resource resource, IExtend iExtend) {
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(resource.getBusinessId());
        if (asset == null) return ;
        String namespace = asset.getAssetKey2();
        String deployment = asset.getAssetKey();
        resource.setAsset(BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class));
        try {
            DatasourceInstance dsInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
            if (dsInstance != null) {
                List<AssetContainer> assetContainers = kubernetesPodProvider.queryAssetsByDeployment(dsInstance.getId(), namespace, deployment);
                resource.setAssetContainers(assetContainers);
            }
        } catch (NullPointerException ignored) {
        }
        applicationResourceInstancePacker.wrap(resource);
    }

}
