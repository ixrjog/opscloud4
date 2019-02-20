package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class RandomUtilTest {


    @Test
    public void test() {
        for (int i = 0; i < 100; i++) {
            System.err.println(RandomUtil.random(2));
        }
    }


    @Test
    public void test2() {

        int att = 100;
        int bj = 150;

        int sum = 0;

        for (int i = 0; i < 100; i++) {
            int a;
            if (RandomUtil.success(5)) {
                a = bj;
                System.err.println("b：" + bj);
            } else {
                a = att;
                System.err.println("s：" + att);
            }
            sum += a;
        }

        System.err.println(sum);


    }


}
