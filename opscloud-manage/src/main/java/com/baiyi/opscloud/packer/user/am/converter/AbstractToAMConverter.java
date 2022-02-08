package com.baiyi.opscloud.packer.user.am.converter;

import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.packer.user.am.AmPacker;
import com.baiyi.opscloud.packer.user.am.IToAMConverter;
import com.baiyi.opscloud.service.datasource.DsConfigService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2022/2/8 1:42 PM
 * @Version 1.0
 */
public abstract class AbstractToAMConverter implements IToAMConverter, InitializingBean {

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsConfigService dsConfigService;

    @Resource
    protected DsConfigHelper dsConfigHelper;

    @Override
    public void afterPropertiesSet() throws Exception {
        AmPacker.context.put(getAMType(), this::toAM);
    }

}
