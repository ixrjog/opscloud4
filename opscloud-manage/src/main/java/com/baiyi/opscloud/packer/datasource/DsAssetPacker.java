package com.baiyi.opscloud.packer.datasource;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.baiyi.opscloud.core.asset.IAssetConverter;
import com.baiyi.opscloud.core.asset.factory.AssetConverterFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.packer.IWrapperRelation;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:39 下午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class DsAssetPacker implements IWrapperRelation<DsAssetVO.Asset> {

    private final DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    private final DsInstanceAssetService dsInstanceAssetService;

    private final DsInstanceAssetRelationService dsInstanceAssetRelationService;

    public void wrap(DsAssetVO.IDsAsset iDsAsset) {
        if (iDsAsset.getAssetId() == 0) {
            return;
        }
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(iDsAsset.getAssetId());
        if (asset == null) {
            return;
        }
        iDsAsset.setAsset(BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class));
    }

    public DsAssetVO.Asset wrap(DsInstanceVO.Instance instance, DatasourceInstanceAsset dsInstanceAsset) {
        DsAssetVO.Asset asset = BeanCopierUtil.copyProperties(dsInstanceAsset, DsAssetVO.Asset.class);
        asset.setDsInstance(instance);
        return asset;
    }

    @Override
    @TagsWrapper
    public void wrap(DsAssetVO.Asset asset, IExtend iExtend, IRelation iRelation) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        // asset properties
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
        // 资产可转换为业务对象
        IAssetConverter converter = AssetConverterFactory.getConverterByAssetType(asset.getAssetType());
        if (converter != null) {
            asset.setConvertBusinessTypes(converter.toBusinessTypes(asset));
        }
        // 关系
        wrap(asset, iRelation);
        asset.setTree(wrapTree(asset));
    }

    @Override
    @TagsWrapper
    public void wrap(DsAssetVO.Asset asset, IExtend iExtend) {
        if (!ExtendUtil.isExtend(iExtend)) {
            return;
        }
        // asset properties
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
        // 资产可转换为业务对象
        IAssetConverter converter = AssetConverterFactory.getConverterByAssetType(asset.getAssetType());
        if (converter != null) {
            asset.setConvertBusinessTypes(converter.toBusinessTypes(asset));
        }
        asset.setTree(wrapTree(asset));
    }

    private void wrap(DsAssetVO.Asset asset, IRelation iRelation) {
        if (!iRelation.isRelation()) {
            return;
        }
        Map<String, List<DsAssetVO.Asset>> children = Maps.newHashMap();
        List<DatasourceInstanceAssetRelation> relations = dsInstanceAssetRelationService.queryTargetAsset(asset.getInstanceUuid(), asset.getId());
        relations.forEach(e -> {
            DsAssetVO.Asset targetAsset = BeanCopierUtil.copyProperties(dsInstanceAssetService.getById(e.getTargetAssetId()), DsAssetVO.Asset.class);
            if (children.containsKey(e.getRelationType())) {
                children.get(e.getRelationType()).add(targetAsset);
            } else {
                children.put(e.getRelationType(), Lists.newArrayList(targetAsset));
            }
        });
        asset.setChildren(children);
    }

    private Map<String, List<DsAssetVO.Asset>> wrapTree(DsAssetVO.Asset asset) {
        List<DatasourceInstanceAsset> assetList = dsInstanceAssetService.listByParentId(asset.getId());
        if (CollectionUtils.isEmpty(assetList)) {
            return Collections.emptyMap();
        }
        List<DsAssetVO.Asset> assetVOList = BeanCopierUtil.copyListProperties(assetList, DsAssetVO.Asset.class);
        assetVOList.forEach(a -> a.setChildren(wrapTree(a)));
        return assetVOList.stream().collect(Collectors.groupingBy(DsAssetVO.Asset::getAssetType));
    }
}
