package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by liangjian on 2017/9/8.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class TimeViewUtilsTest {


    @Test
    public void test() {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:m:s");
            Date date = format.parse("2017-09-08 08:35:35");
            System.out.println(TimeViewUtils.format(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testEmail() {
        int l = 3;
        for (int i = 0; i <=210; i++) {
            String mail="study";
            String x = "" + i;
            if (x.length() == 1) {
                x = "00" + x;
            }

            if (x.length() == 2) {
                x = "0" + x;
            }

            System.err.println("study" +x +".com REJECT");



        }


    }
}
