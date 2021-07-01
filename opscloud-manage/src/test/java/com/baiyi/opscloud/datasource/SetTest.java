package com.baiyi.opscloud.datasource;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.type.DsTypeEnum;
import com.baiyi.opscloud.datasource.factory.SetDsInstanceConfigFactory;
import com.baiyi.opscloud.datasource.provider.base.common.AbstractSetDsInstanceConfigProvider;
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
