package com.baiyi.opscloud.datasource.provider.asset;

import com.baiyi.opscloud.datasource.asset.IAssetConvert;
import com.baiyi.opscloud.datasource.asset.factory.AssetConvertFactory;
import com.baiyi.opscloud.datasource.provider.base.asset.IAssetBusinessRelation;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 *
 * 资产与业务对象绑定
 *
 * @Author baiyi
 * @Date 2021/8/2 2:00 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractAssetBusinessRelationProvider<T> extends BaseAssetProvider<T> implements IAssetBusinessRelation {

    @Override
    public void scan(DsAssetVO.Asset asset) {
        IAssetConvert iAssetConvert = AssetConvertFactory.getIAssetConvertByAssetType(getAssetType());
        if (iAssetConvert == null) return;
        // 获取可转换的业务对象
        Map<BusinessTypeEnum, BusinessAssetRelationVO.IBusinessAssetRelation> convertBusinessTypes = iAssetConvert.toBusinessTypes(asset);
        convertBusinessTypes.keySet().forEach(k->{



        });
    }

}
