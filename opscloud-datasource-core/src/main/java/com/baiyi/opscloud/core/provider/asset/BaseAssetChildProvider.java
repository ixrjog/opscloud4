package com.baiyi.opscloud.core.provider.asset;

import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/12/14 5:43 PM
 * @Version 1.0
 */
public abstract class BaseAssetChildProvider<C> extends BaseAssetProvider<C> {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;

}
