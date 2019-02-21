package com.sdg.cmdb.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class PasswdUtilsTest {


    @Test
    public void test() {
        System.err.println(PasswdUtils.getRandomPasswd(50));

    }
}
