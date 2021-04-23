package com.baiyi.opscloud.ansible;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.ansible.handler.AnsibleExecutorHandler;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.vo.server.PreviewAttributeVO;
import com.baiyi.opscloud.facade.impl.AttributeFacadeImpl;
import com.baiyi.opscloud.factory.attribute.impl.AnsibleAttribute;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.google.common.base.Joiner;
import com.google.common.collect.Sets;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author baiyi
 * @Date 2020/4/6 8:52 下午
 * @Version 1.0
 */
public class AnsibleTest extends BaseUnit {
    @Resource
    private AnsibleExecutorHandler ansibleExecutor;

    @Resource
    private AnsibleAttribute ansibleAttribute;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private AttributeFacadeImpl attributeFacadeImpl;

    @Test
    void aTest() {
        String str = "10.200.1.40 | Cdsdg => { aaa , bbb , ccc }" ;

        Pattern pattern = Pattern.compile("^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\ \\|\\ \\w+\\ =>\\ ");
        Matcher matcher = pattern.matcher(str);
        if (matcher.find()) {
            String collegeId = matcher.group(0);

            System.out.println(collegeId );//14000
        }
    }


    @Test
    void bTest() {
        Set<String>  tags = Sets.newHashSet();
        tags.add("test1");
        tags.add("test2");
        tags.add("test3");
        System.err.println( Joiner.on(",").join(tags));
    }

    @Test
    void aaaTest() {

        long startTime = new Date().getTime();
        attributeFacadeImpl.createAnsibleHostsTask();
        long endTime = new Date().getTime();
        System.err.println("消耗时间:" + (endTime - startTime));

    }

    @Test
    void executorTest() {
        ansibleExecutor.executorTest(100000L);
        System.err.println("111111");
    }

    @Test
    void testAnsiblePreviewAttribute() {
        // 1423
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(46);
        List<PreviewAttributeVO.PreviewAttribute> list = ansibleAttribute.preview(ocServerGroup.getId());
        for (PreviewAttributeVO.PreviewAttribute preview : list)
            System.err.println(preview.getContent());


        PreviewAttributeVO.PreviewAttribute pre = ansibleAttribute.build(ocServerGroup);
        System.err.println(pre.getContent());


    }

    @Test
    void testAnsibleGrouping() {
        // 1423
        OcServerGroup ocServerGroup = ocServerGroupService.queryOcServerGroupById(46);
        ansibleAttribute.evictGrouping(ocServerGroup);

        for (int i = 1; i <= 11; i++) {
            long startTime = new Date().getTime();
            Map<String, List<OcServer>> map = ansibleAttribute.grouping(ocServerGroup);
            long endTime = new Date().getTime();
            System.err.println("第" + i + "次, 消耗时间:" + (endTime - startTime));
        }
    }
}
