package com.sdg.cmdb.service.impl;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ProjectServiceImplTest {

    @Resource
    private ProjectServiceImpl projectServiceImpl;

    @Test
    public void testTimeView() {
        try {
            Date d = new Date();

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String dateString = format.format(d);
            System.err.println(dateString);

//            SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
//            Date timeViewDate = format2.parse(d);
//
//            System.err.println(dateString + "<" + TimeViewUtils.format(timeViewDate) + ">");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testTask() {
        projectServiceImpl.task();
    }
}
