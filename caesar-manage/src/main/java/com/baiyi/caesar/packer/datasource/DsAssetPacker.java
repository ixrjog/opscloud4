package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetProperty;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetRelation;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.param.IRelation;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetRelationService;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import com.baiyi.caesar.util.ExtendUtil;
import com.baiyi.caesar.util.RelationUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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

    public List<DsAssetVO.Asset> wrapVOList(List<DatasourceInstanceAsset> data, IExtend iExtend, IRelation iRelation) {
        return data.stream().map(e -> {
            DsAssetVO.Asset asset = toVO(e);
            if (ExtendUtil.isExtend(iExtend)) {
                wrap(asset);
                if (RelationUtil.isRelation(iRelation))
                    wrapRelation(asset);
            }
            return asset;
        }).collect(Collectors.toList());
    }

    private void wrap(DsAssetVO.Asset asset) {
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
    }

    private DsAssetVO.Asset toVO(DatasourceInstanceAsset asset) {
        return BeanCopierUtil.copyProperties(asset, DsAssetVO.Asset.class);
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

}
