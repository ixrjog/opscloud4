package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class KeyPairGenUtilTest {


    //geration
    @Test
    public void test1() {
        String s = "";
        System.err.println("原文:" + s);
        System.err.println(KeyPairGenUtil.encrypt(s));
    }

    @Test
    public void test2() {
        System.err.println(KeyPairGenUtil.decrypt(""));
    }

}
