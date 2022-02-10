package com.baiyi.opscloud.packer.datasource;

import com.baiyi.opscloud.common.annotation.TagsWrapper;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.core.asset.IAssetConverter;
import com.baiyi.opscloud.core.asset.factory.AssetConvertFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAsset;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetProperty;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstanceAssetRelation;
import com.baiyi.opscloud.domain.param.IExtend;
import com.baiyi.opscloud.domain.param.IRelation;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import com.baiyi.opscloud.domain.vo.datasource.DsInstanceVO;
import com.baiyi.opscloud.packer.TagPacker;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.common.util.ExtendUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
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
public class DsAssetPacker {

    @Resource
    private DsInstanceAssetPropertyService dsInstanceAssetPropertyService;

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsInstanceAssetRelationService dsInstanceAssetRelationService;

    @Resource
    private TagPacker tagPacker;

    public void wrap(DsAssetVO.IDsAsset iDsAsset) {
        if (iDsAsset.getAssetId() == 0) return;
        DatasourceInstanceAsset asset = dsInstanceAssetService.getById(iDsAsset.getAssetId());
        if (asset == null) return;
        iDsAsset.setAsset(toVO(asset));
    }

    public DsAssetVO.Asset wrap(DsInstanceVO.Instance instance, DatasourceInstanceAsset dsInstanceAsset) {
        DsAssetVO.Asset asset = BeanCopierUtil.copyProperties(dsInstanceAsset, DsAssetVO.Asset.class);
        asset.setDsInstance(instance);
        return asset;
    }

//    public List<DsAssetVO.Asset> wrapVOList(List<DatasourceInstanceAsset> data, IExtend iExtend, IRelation iRelation) {
//        return data.stream().map(e ->
//                wrapVO(e, iExtend, iRelation)
//        ).collect(Collectors.toList());
//    }

    @TagsWrapper
    public void wrap(DsAssetVO.Asset asset, IExtend iExtend, IRelation iRelation) {
        if (ExtendUtil.isExtend(iExtend)) {
            wrap(asset);
            wrapConvertBusinessTypes(asset); // 资产可转换为业务对象
            if (iRelation.isRelation())
                wrapRelation(asset);
            asset.setTree(wrapTree(asset));
        }
    }


    public DsAssetVO.Asset wrapVO(DatasourceInstanceAsset datasourceInstanceAsset, IExtend iExtend, IRelation iRelation) {
        DsAssetVO.Asset asset = toVO(datasourceInstanceAsset);
        if (ExtendUtil.isExtend(iExtend)) {
            // tagPacker.wrap(asset);
            wrap(asset);
            wrapConvertBusinessTypes(asset); // 资产可转换为业务对象
            if (iRelation.isRelation())
                wrapRelation(asset);
            asset.setTree(wrapTree(asset));
        }
        return asset;
    }

    //  to   AssetConvertFactory
    private void wrapConvertBusinessTypes(DsAssetVO.Asset asset) {
        IAssetConverter iAssetConvert = AssetConvertFactory.getIAssetConvertByAssetType(asset.getAssetType());
        if (iAssetConvert != null) {
            asset.setConvertBusinessTypes(iAssetConvert.toBusinessTypes(asset));
        }
    }


    // asset properties
    private void wrap(DsAssetVO.Asset asset) {
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
    }

    private DsAssetVO.Asset toVO(DatasourceInstanceAsset asset) {
        return BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class);
    }

    private List<DsAssetVO.Asset> toVOList(List<DatasourceInstanceAsset> assetList) {
        return BeanCopierUtil.copyListProperties(assetList, DsAssetVO.Asset.class);
    }

    private void wrapRelation(DsAssetVO.Asset asset) {
        Map<String, List<DsAssetVO.Asset>> children = Maps.newHashMap();
        List<DatasourceInstanceAssetRelation> relations = dsInstanceAssetRelationService.queryTargetAsset(asset.getInstanceUuid(), asset.getId());
        relations.forEach(e -> {
            DsAssetVO.Asset targetAsset = toVO(dsInstanceAssetService.getById(e.getTargetAssetId()));
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
        if (CollectionUtils.isEmpty(assetList))
            return Collections.emptyMap();
        List<DsAssetVO.Asset> assetVOList = toVOList(assetList);
        assetVOList.forEach(a -> a.setChildren(wrapTree(a)));
        return assetVOList.stream().collect(Collectors.groupingBy(DsAssetVO.Asset::getAssetType));
    }
}
