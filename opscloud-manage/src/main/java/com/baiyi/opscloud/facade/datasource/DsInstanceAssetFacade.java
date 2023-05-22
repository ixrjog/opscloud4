package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.param.datasource.DsAssetParam;
import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/5 11:00 上午
 * @Version 1.0
 */
public interface DsInstanceAssetFacade {

    DataTable<DsAssetVO.Asset> queryAssetPage(DsAssetParam.AssetPageQuery pageQuery);

    /**
     * 查询用户密钥
     *
     * @param username
     * @return
     */
    List<DsAssetVO.Asset> querySshKeyAssets(String username);

    void deleteAssetByAssetId(int assetId);

    /**
     * 删除数据源实例下指定类型的所有资产
     * @param instanceId
     * @param assetType
     */
    void deleteAssetByType(int instanceId, String assetType);

    void setAssetActiveByAssetId(int assetId);

}
