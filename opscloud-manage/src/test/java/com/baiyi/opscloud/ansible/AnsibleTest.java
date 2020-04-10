package com.baiyi.opscloud.ansible;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.ansible.handler.AnsibleExecutorHandler;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.factory.attribute.impl.AttributeAnsible;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2020/4/6 8:52 下午
 * @Version 1.0
 */
public class AnsibleTest extends BaseUnit {
    @Resource
    private AnsibleExecutorHandler ansibleExecutor;

    @Resource
    private AttributeAnsible attributeAnsible;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Test
    void executorTest() {
        //CommandLine commandLine = CommandLine.parse("ping www.baidu.com");

        ansibleExecutor.executorTest(100000L);
        System.err.println("111111");
    }

    @Test
    void testAnsibleGrouping() {
        // 1423
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(46);
        attributeAnsible.evictGrouping(ocServerGroup);

        for (int i = 1; i <= 11; i++) {
            long startTime = new Date().getTime();
            Map<String, List<OcServer>> map = attributeAnsible.grouping(ocServerGroup);
            long endTime = new Date().getTime();
            System.err.println("第" + i + "次, 消耗时间:" + (endTime - startTime));
        }

    }
}
