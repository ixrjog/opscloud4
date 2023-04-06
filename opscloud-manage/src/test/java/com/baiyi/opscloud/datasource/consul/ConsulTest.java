package com.baiyi.opscloud.datasource.consul;

import com.baiyi.opscloud.datasource.consul.base.BaseConsulUnit;
import com.baiyi.opscloud.datasource.consul.driver.ConsulServiceDriver;
import com.baiyi.opscloud.datasource.consul.entity.ConsulHealth;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/7/26 09:40
 * @Version 1.0
 */
public class ConsulTest extends BaseConsulUnit {

    @Resource
    private ConsulServiceDriver consulServiceDriver;



    @Test
    void listHealthServiceTest() {
        List<ConsulHealth.Health> healths = consulServiceDriver.listHealthService(getConfig().getConsul(),"account","dc1");
    }

}