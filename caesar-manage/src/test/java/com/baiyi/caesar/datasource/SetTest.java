package com.baiyi.caesar.datasource;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.common.type.DsTypeEnum;
import com.baiyi.caesar.datasource.factory.SetDsInstanceConfigFactory;
import com.baiyi.caesar.datasource.provider.base.common.AbstractSetDsInstanceConfigProvider;
import org.junit.jupiter.api.Test;

/**
 * @Author baiyi
 * @Date 2021/6/24 7:42 下午
 * @Version 1.0
 */
public class SetTest extends BaseUnit {

    @Test
    void setConfigTest() {
        AbstractSetDsInstanceConfigProvider setDsInstanceConfigProvider = SetDsInstanceConfigFactory.getProvider(DsTypeEnum.KUBERNETES.getName());
        setDsInstanceConfigProvider.setConfig(5);
    }
}
