package com.baiyi.opscloud.facade.datasource;

import com.baiyi.opscloud.domain.vo.datasource.DsAssetVO;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/7/5 11:00 上午
 * @Version 1.0
 */
public interface DsInstanceAssetFacade {

    /**
     *  查询用户密钥
     * @param username
     * @return
     */
    List<DsAssetVO.Asset> querySshKeyAssets(String username);

}
