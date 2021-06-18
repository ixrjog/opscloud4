package com.baiyi.caesar.packer.datasource;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstanceAsset;
import com.baiyi.caesar.domain.param.IExtend;
import com.baiyi.caesar.domain.vo.datasource.DsAssetVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/6/18 5:39 下午
 * @Version 1.0
 */
public class DsAssetPacker {

    public static List<DsAssetVO.Asset> wrapVOList(List<DatasourceInstanceAsset> data, IExtend iExtend) {
        List<DsAssetVO.Asset> voList = BeanCopierUtil.copyListProperties(data, DsAssetVO.Asset.class);
        return voList;
    }
}
