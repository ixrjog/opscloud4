package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.kubernetes.handler.KubernetesPodHandler;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.constants.DsAssetTypeConstants;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.leo.LeoBuildImageService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/2/18 2:01 PM
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationResourcePacker implements IWrapper<ApplicationResourceVO.Resource> {

    private final KubernetesPodHandler kubernetesPodHandler;

    private final DsInstanceAssetService assetService;

    private final DsInstanceAssetPropertyService propertyService;

    private final DsInstanceService dsInstanceService;

    private final ApplicationResourceDsInstancePacker applicationResourceInstancePacker;

    private final DsInstanceAssetRelationService assetRelationService;

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    private final LeoBuildImageService leoBuildImageService;

    private final static String IMAGE = "image";

    private final static String ZONE = "labels:failure-domain.beta.kubernetes.io/zone";

    /**
     * Deployment Pod
     *
     * @param resource
     */
    @Override
    public void wrap(ApplicationResourceVO.Resource resource, IExtend iExtend) {
        DatasourceInstanceAsset datasourceInstanceAsset = assetService.getById(resource.getBusinessId());
        if (datasourceInstanceAsset == null) {
            return;
        }
        final String namespace = datasourceInstanceAsset.getAssetKey2();
        final String deployment = datasourceInstanceAsset.getAssetKey();

        resource.setAsset(getAsset(datasourceInstanceAsset));
        try {
            DatasourceInstance dsInstance = dsInstanceService.getByUuid(datasourceInstanceAsset.getInstanceUuid());
            if (dsInstance != null) {
                // 插入版本信息
                List<AssetContainer> assetContainers = kubernetesPodHandler.queryAssetsByDeployment(dsInstance.getId(), namespace, deployment)
                        .stream()
                        .peek(c -> fillProperties(resource, c))
                        .collect(Collectors.toList());
                resource.setAssetContainers(assetContainers);
                resource.setTags(null);
            }
        } catch (NullPointerException ignored) {
        }
        wrapTags(resource, datasourceInstanceAsset);
        applicationResourceInstancePacker.wrap(resource);
    }

    private void fillProperties(ApplicationResourceVO.Resource resource, AssetContainer c) {
        if (c.getProperties().containsKey(IMAGE)) {
            final String image = c.getProperties().get(IMAGE);
            LeoBuildImage buildImage = leoBuildImageService.getByImage(image);
            if (buildImage != null) {
                c.getProperties().put("versionName", buildImage.getVersionName());
                c.getProperties().put("versionDesc", buildImage.getVersionDesc());
            } else {
                c.getProperties().put("versionName", "unknown");
            }
        }
        if (c.getProperties().containsKey("nodeName")) {
            final String nodeName = c.getProperties().get("nodeName");
            final String instanceUuid = resource.getAsset().getInstanceUuid();
            DatasourceInstanceAsset asset = DatasourceInstanceAsset.builder()
                    .instanceUuid(instanceUuid)
                    .name(nodeName)
                    .assetType(DsAssetTypeConstants.KUBERNETES_NODE.name())
                    .build();

            List<DatasourceInstanceAsset> nodes = assetService.queryAssetByAssetParam(asset);
            if (!CollectionUtils.isEmpty(nodes) && nodes.size() == 1) {
                DatasourceInstanceAssetProperty zoneProperty = propertyService.getByUniqueKey(nodes.getFirst().getId(), ZONE);
                if (zoneProperty != null) {
                    c.getProperties().put("zone", "zone:" + zoneProperty.getValue());
                }
            }

        }
    }

    private DsAssetVO.Asset getAsset(DatasourceInstanceAsset datasourceInstanceAsset) {
        DsAssetVO.Asset asset = BeanCopierUtil.copyProperties(datasourceInstanceAsset, DsAssetVO.Asset.class);
        if (DsAssetTypeConstants.KUBERNETES_DEPLOYMENT.name().equals(asset.getAssetType())) {
            Map<String, String> properties = propertyService.queryByAssetId(asset.getId())
                    .stream()
                    .collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
            asset.setProperties(properties);
        }
        return asset;
    }

    public void wrapProperties(ApplicationResourceVO.Resource resource) {
        if (resource.getBusinessType() != BusinessTypeEnum.ASSET.getType()) {
            return;
        }
        DsAssetVO.Asset asset = BeanCopierUtil.copyProperties(assetService.getById(resource.getBusinessId()), DsAssetVO.Asset.class);
        Map<String, String> properties = propertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
        resource.setAsset(asset);
    }

    private void wrapTags(ApplicationResourceVO.Resource resource, DatasourceInstanceAsset asset) {
        resource.setTags(acqTags(asset));
    }

    public List<TagVO.Tag> acqTags(DatasourceInstanceAsset asset) {
        List<DatasourceInstanceAssetRelation> assetRelations = assetRelationService.queryTargetAsset(asset.getInstanceUuid(), asset.getId());
        if (CollectionUtils.isEmpty(assetRelations)) {
            return Lists.newArrayList();
        }
        Set<Integer> tagIdSet = Sets.newHashSet();
        assetRelations.stream().map(e ->
                        assetService.getById(e.getTargetAssetId())).map(targetAsset -> businessTagService.queryByBusiness(SimpleBusiness.builder()
                        .businessType(BusinessTypeEnum.ASSET.getType())
                        .businessId(targetAsset.getId())
                        .build()))
                .filter(businessTags -> !CollectionUtils.isEmpty(businessTags))
                .forEach(businessTags -> businessTags.forEach(t -> tagIdSet.add(t.getTagId())));
        return tagIdSet.stream().map(tagId ->
                BeanCopierUtil.copyProperties(tagService.getById(tagId), TagVO.Tag.class)
        ).collect(Collectors.toList());
    }

}
