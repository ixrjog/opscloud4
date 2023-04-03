package com.baiyi.opscloud.datasource.zabbix.v5;

import com.baiyi.opscloud.datasource.zabbix.base.BaseZabbixTest;
import com.baiyi.opscloud.zabbix.v5.driver.ZabbixV5ProblemDriver;
import com.baiyi.opscloud.zabbix.v5.entity.ZabbixProblem;
import com.baiyi.opscloud.zabbix.constant.SeverityType;
import com.google.common.collect.Lists;
import org.junit.jupiter.api.Test;

import jakarta.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/11/22 10:30 上午
 * @Version 1.0
 */
public class ZabbixProblemTest extends BaseZabbixTest {

    @Resource
    private ZabbixV5ProblemDriver zabbixV5ProblemDatasource;

    private static final List<SeverityType> SEVERITY_TYPES = Lists.newArrayList(
            SeverityType.DEFAULT,
            SeverityType.INFORMATION,
            SeverityType.WARNING,
            SeverityType.HIGH,
            SeverityType.DISASTER);

    @Test
    void listTest() {
        List<ZabbixProblem.Problem> problems = zabbixV5ProblemDatasource.list(getConfig().getZabbix(), SEVERITY_TYPES);
        print(problems);
    }

}