package com.baiyi.caesar.datasource.base.common;

import com.baiyi.caesar.datasource.model.DsInstanceContext;
import com.baiyi.caesar.domain.generator.caesar.DatasourceInstance;
import com.baiyi.caesar.service.datasource.DsConfigService;
import com.baiyi.caesar.service.datasource.DsInstanceService;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/6/23 11:11 上午
 * @Version 1.0
 */
public abstract class SimpleDsInstanceProvider {

    @Resource
    private DsInstanceService dsInstanceService;

    @Resource
    private DsConfigService dsConfigService;

    protected DsInstanceContext buildDsInstanceContext(int dsInstanceId) {
        DatasourceInstance dsInstance = dsInstanceService.getById(dsInstanceId);
        return DsInstanceContext.builder()
                .dsInstance(dsInstance)
                .dsConfig(dsConfigService.getById(dsInstance.getConfigId()))
                .build();
    }

}
