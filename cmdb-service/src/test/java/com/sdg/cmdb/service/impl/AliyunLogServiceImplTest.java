package com.sdg.cmdb.service.impl;


import com.sdg.cmdb.util.TimeUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.ParseException;



/**
 * Created by liangjian on 2017/9/18.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AliyunLogServiceImplTest {

    @Resource
    private AliyunLogServiceImpl aliyunLogServiceImpl;



    @Test
    public void testReadLog() {
        aliyunLogServiceImpl.readLog("collect-nginx-logs","ka-www");

    }

    @Test
    public void testTime()throws ParseException {
        //1505908600
        //1505908740
        System.err.println(TimeUtils.dateToStamp("2017-09-20 19:59:00"));
    }


}
