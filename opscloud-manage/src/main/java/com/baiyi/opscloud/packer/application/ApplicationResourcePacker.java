package com.baiyi.opscloud.packer.application;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.datasource.kubernetes.provider.KubernetesPodProvider;
import com.baiyi.opscloud.domain.base.SimpleBusiness;
import com.baiyi.opscloud.domain.builder.asset.AssetContainer;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.vo.application.ApplicationResourceVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.tag.TagVO;
import com.baiyi.opscloud.packer.IWrapper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.service.tag.BusinessTagService;
import com.baiyi.opscloud.service.tag.TagService;
import com.github.xiaoymin.knife4j.core.util.CollectionUtils;
import com.google.common.collect.Sets;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
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

    private final KubernetesPodProvider kubernetesPodProvider;

    private final DsInstanceAssetService assetService;

    private final DsInstanceService dsInstanceService;

    private final ApplicationResourceDsInstancePacker applicationResourceInstancePacker;

    private final DsInstanceAssetRelationService assetRelationService;

    private final TagService tagService;

    private final BusinessTagService businessTagService;

    /**
     * Deployment Pod
     *
     * @param resource
     */
    @Override
    public void wrap(ApplicationResourceVO.Resource resource, IExtend iExtend) {
        DatasourceInstanceAsset asset = assetService.getById(resource.getBusinessId());
        if (asset == null) return;
        String namespace = asset.getAssetKey2();
        String deployment = asset.getAssetKey();
        resource.setAsset(BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class));
        try {
            DatasourceInstance dsInstance = dsInstanceService.getByUuid(asset.getInstanceUuid());
            if (dsInstance != null) {
                List<AssetContainer> assetContainers = kubernetesPodProvider.queryAssetsByDeployment(dsInstance.getId(), namespace, deployment);
                resource.setAssetContainers(assetContainers);
                resource.setTags(null);
            }
        } catch (NullPointerException ignored) {
        }
        wrapTags(resource, asset);
        applicationResourceInstancePacker.wrap(resource);
    }

    private void wrapTags(ApplicationResourceVO.Resource resource, DatasourceInstanceAsset asset) {
        List<DatasourceInstanceAssetRelation> assetRelations = assetRelationService.queryTargetAsset(asset.getInstanceUuid(),asset.getId());
        if (CollectionUtils.isEmpty(assetRelations)) return;
        Set<Integer> tagIdSet = Sets.newHashSet();
        assetRelations.stream().map(e ->
                assetService.getById(e.getTargetAssetId())).map(targetAsset -> businessTagService.queryByBusiness(SimpleBusiness.builder()
                .businessType(BusinessTypeEnum.ASSET.getType())
                .businessId(targetAsset.getId())
                .build())).filter(businessTags -> !CollectionUtils.isEmpty(businessTags)).forEach(businessTags -> businessTags.forEach(t -> tagIdSet.add(t.getTagId())));
        List<TagVO.Tag> tags = tagIdSet.stream().map(tagId ->
                BeanCopierUtil.copyProperties(tagService.getById(tagId), TagVO.Tag.class)
        ).collect(Collectors.toList());
        resource.setTags(tags);
    }

}
