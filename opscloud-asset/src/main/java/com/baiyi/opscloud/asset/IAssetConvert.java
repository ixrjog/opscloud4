package com.baiyi.opscloud.asset;

import com.baiyi.opscloud.datasource.provider.base.asset.IAssetType;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/30 1:22 下午
 * @Version 1.0
 */
public interface IAssetConvert<T> extends IAssetType {

    Map<BusinessTypeEnum,T> toBusinessTypes(DsAssetVO.Asset asset);
}
