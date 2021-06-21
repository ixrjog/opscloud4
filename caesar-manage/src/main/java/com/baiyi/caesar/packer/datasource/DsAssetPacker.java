package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAssetProperty;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;
import com.baiyi.caesar.service.datasource.DsInstanceAssetPropertyService;
import com.baiyi.caesar.util.ExtendUtil;
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

    public List<DsAssetVO.Asset> wrapVOList(List<DatasourceInstanceAsset> data, IExtend iExtend) {
        List<DsAssetVO.Asset> voList = BeanCopierUtil.copyListProperties(data, DsAssetVO.Asset.class);
        if (!ExtendUtil.isExtend(iExtend))
            return voList;
        return voList.stream().peek(this::wrap).collect(Collectors.toList());
    }

    private void wrap(DsAssetVO.Asset asset) {
        Map<String, String> properties = dsInstanceAssetPropertyService.queryByAssetId(asset.getId())
                .stream().collect(Collectors.toMap(DatasourceInstanceAssetProperty::getName, DatasourceInstanceAssetProperty::getValue, (k1, k2) -> k1));
        asset.setProperties(properties);
    }

}
