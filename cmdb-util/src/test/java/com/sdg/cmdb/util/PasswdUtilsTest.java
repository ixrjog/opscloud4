package com.sdg.cmdb.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class PasswdUtilsTest {

    @Test
    public void test() {
        System.err.println(PasswdUtils.getRandomPasswd(50));
    }

    @Test
    public void test2() {
        Random random = new Random();
        for (int i = 0; i <= 20; i++)
            System.err.println(random.nextInt(3));
    }

    @Test
    public void test3() {
       // String p = PasswdUtils.getPassword(20);

        for (int i = 0; i <= 1; i++) {
            String p = PasswdUtils.getPassword(20);
            System.err.println(p + "  " + RegexUtils.checkPassword(p));
        }
    }
}
