package com.baiyi.caesar.datasource.facade.impl;

import com.baiyi.caesar.datasource.facade.DsAssetFacade;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/17 2:01 下午
 * @Version 1.0
 */
@Service
public class DsAssetFacadeImpl implements DsAssetFacade {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;
}
