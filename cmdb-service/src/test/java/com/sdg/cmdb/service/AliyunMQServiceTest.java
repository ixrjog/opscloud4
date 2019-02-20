package com.sdg.cmdb.service;



import com.sdg.cmdb.domain.aliyunMQ.OnsTopicVO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunMQServiceTest {

    @Resource
    private AliyunMQService aliyunMQService;

    @Test
    public void testTopicList() {
        List<OnsTopicVO> voList = aliyunMQService.topicList("", null);
        for (OnsTopicVO vo : voList) {
            System.out.println(vo.getTopic() + "     " + vo.getOwner());
        }
    }

    @Test
    public void testTask() {
        aliyunMQService.task();
    }
}
