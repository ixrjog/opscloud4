package com.baiyi.opscloud.factory.business.base;

import com.baiyi.opscloud.domain.vo.business.BaseBusiness;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author baiyi
 * @Date 2021/9/8 10:03 上午
 * @Version 1.0
 */
public interface IBusinessService<T> extends BaseBusiness.IBusinessType {

    T getById(Integer id);

    T getByKey(String key);

    BusinessAssetRelationVO.IBusinessAssetRelation toBusinessAssetRelation(DsAssetVO.Asset asset);

}
