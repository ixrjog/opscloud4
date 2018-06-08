package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class TimeUtilsTest {


    @Test
    public void test() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = format.parse("2017-09-08 08:35:35");
            System.err.println(TimeUtils.calculateDateDiff4Day(date, new Date()));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
