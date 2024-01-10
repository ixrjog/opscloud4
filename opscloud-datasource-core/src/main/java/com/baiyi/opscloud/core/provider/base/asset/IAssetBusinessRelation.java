package com.baiyi.opscloud.core.provider.base.asset;

import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

/**
 * @Author baiyi
 * @Date 2021/8/2 2:03 下午
 * @Version 1.0
 */
public interface IAssetBusinessRelation {

    /**
     * 扫描所有资产与业务对象绑定关系
     * @param dsInstanceId
     */
    void scan(int dsInstanceId);

    /**
     * 扫描资产与业务对象绑定关系
     * @param asset
     */
    void scan(DsAssetVO.Asset asset);

}