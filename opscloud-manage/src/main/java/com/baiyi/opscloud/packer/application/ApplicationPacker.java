package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.kubernetes.provider.KubernetesPodProvider;
import com.baiyi.opscloud.datasource.packer.DsInstancePacker;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.application.ApplicationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.packer.business.BusinessPermissionUserPacker;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/7/12 1:23 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationPacker {

    private final KubernetesPodProvider kubernetesPodProvider;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceService dsInstanceService;

    private final ApplicationResourceService applicationResourceService;

    private final DsInstancePacker dsInstancePacker;

    private final BusinessPermissionUserPacker businessPermissionUserPacker;

    public List<ApplicationVO.Application> wrapVOList(List<Application> data) {
        return BeanCopierUtil.copyListProperties(data, ApplicationVO.Application.class);
    }

    public List<ApplicationVO.Application> wrapVOList(List<Application> data, IExtend iExtend) {
        return data.stream().map(e -> wrapVO(e, iExtend)).collect(Collectors.toList());
    }

    /**
     * Kubernetes专用
     *
     * @param data
     * @return
     */
    public List<ApplicationVO.Application> wrapVOListByKubernetes(List<Application> data) {
        List<ApplicationVO.Application> voList = toVOList(data);
        voList.forEach(a -> {
            List<ApplicationResource> resources = applicationResourceService.queryByApplication(a.getId(), DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name());
            a.setResources(resources.stream().map(this::wrapPodByDeployment).collect(Collectors.toList()));
        });
        return voList;
    }

    private ApplicationResourceVO.Resource wrapPodByDeployment(ApplicationResource resource) {
        ApplicationResourceVO.Resource resourceVO = BeanCopierUtil.copyProperties(resource, ApplicationResourceVO.Resource.class);
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(resource.getBusinessId());
        if (asset == null) return resourceVO;
        String namespace = asset.getAssetKey2();
        String deployment = asset.getAssetKey();
        resourceVO.setAsset(BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class));
        try {
            DatasourceInstance dsInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
            if (dsInstance != null) {
                List<AssetContainer> assetContainers = kubernetesPodProvider.queryAssetsByDeployment(dsInstance.getId(), namespace, deployment);
                resourceVO.setAssetContainers(assetContainers);
            }
        } catch (NullPointerException ignored) {
        }
        wrapResourceInstance(resourceVO);
        return resourceVO;
    }

    private List<ApplicationVO.Application> toVOList(List<Application> data) {
        return BeanCopierUtil.copyListProperties(data, ApplicationVO.Application.class);
    }

    public ApplicationVO.Application wrapVO(Application application, IExtend iExtend) {
        ApplicationVO.Application vo = BeanCopierUtil.copyProperties(application, ApplicationVO.Application.class);
        if (iExtend.getExtend()) {
            List<ApplicationResource> applicationResourceList = applicationResourceService.queryByApplication(application.getId());
            List<ApplicationResourceVO.Resource> resources = BeanCopierUtil.copyListProperties(applicationResourceList, ApplicationResourceVO.Resource.class);
            resources.forEach(this::wrapResourceInstance);
            Map<String, List<ApplicationResourceVO.Resource>> resourcesMap = resources.stream()
                    .collect(Collectors.groupingBy(ApplicationResourceVO.Resource::getResourceType));
            vo.setResourceMap(resourcesMap);
            businessPermissionUserPacker.wrap(vo);
        }
        return vo;
    }

    private void wrapResourceInstance(ApplicationResourceVO.Resource r) {
        if (r.getBusinessType() == BusinessTypeEnum.ASSET.getType()) {
            DatasourceInstanceAsset asset = dsInstanceAssetService.getById(r.getBusinessId());
            r.setInstanceUuid(asset.getInstanceUuid());
            dsInstancePacker.wrap(r);
        }
    }

}
