package com.sdg.cmdb.util;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitFlowUtilsTest {

    @Test
    public void test() {
        String s="feature/for_test";
       System.err.println(s.matches("feature/.+")) ;
    }
}
