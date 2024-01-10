package com.baiyi.opscloud.factory.business.base;

import com.baiyi.opscloud.domain.base.BaseBusiness;
import com.baiyi.opscloud.domain.vo.business.BusinessAssetRelationVO;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author baiyi
 * @Date 2021/9/8 10:03 上午
 * @Version 1.0
 */
public interface IBusinessService<T> extends BaseBusiness.IBusinessType {

    /**
     * 按id查询
     * @param id
     * @return
     */
    T getById(Integer id);

    /**
     * 按key查询
     * @param key
     * @return
     */
    T getByKey(String key);

    /**
     * 转换
     * @param asset
     * @return
     */
    BusinessAssetRelationVO.IBusinessAssetRelation toBusinessAssetRelation(DsAssetVO.Asset asset);

}