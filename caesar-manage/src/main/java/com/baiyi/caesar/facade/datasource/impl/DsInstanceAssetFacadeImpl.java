package com.baiyi.caesar.facade.datasource.impl;

import com.baiyi.caesar.facade.datasource.DsInstanceAssetFacade;
import com.baiyi.caesar.service.datasource.DsInstanceAssetService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/17 1:44 下午
 * @Version 1.0
 */
@Service
public class DsInstanceAssetFacadeImpl implements DsInstanceAssetFacade {

    @Resource
    private DsInstanceAssetService dsInstanceAssetService;




}
