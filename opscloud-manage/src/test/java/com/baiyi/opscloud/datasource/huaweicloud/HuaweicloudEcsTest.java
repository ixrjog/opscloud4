package com.baiyi.opscloud.datasource.huaweicloud;

import com.baiyi.opscloud.common.datasource.HuaweicloudConfig;
import com.baiyi.opscloud.datasource.huaweicloud.base.BaseHuaweicloudTest;
import com.baiyi.opscloud.datasource.huaweicloud.ecs.driver.HuaweicloudEcsDriver;
import com.baiyi.opscloud.datasource.huaweicloud.ecs.driver.HuaweicloudVpcDriver;
import com.baiyi.opscloud.datasource.huaweicloud.ecs.entity.HuaweicloudEcs;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/7 15:46
 * @Version 1.0
 */
public class HuaweicloudEcsTest extends BaseHuaweicloudTest {

    @Test
    void listVpcTest() {
        HuaweicloudConfig config = getConfig();
        HuaweicloudVpcDriver.listVpcs(config.getHuaweicloud());
    }

    @Test
    void listServerTest() {
        HuaweicloudConfig config = getConfig();
        List<HuaweicloudEcs.Ecs> ecss = HuaweicloudEcsDriver.listServers("ap-southeast-1", config.getHuaweicloud());
        print(ecss);
    }

}
