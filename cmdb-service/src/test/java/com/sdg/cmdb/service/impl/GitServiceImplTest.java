package com.sdg.cmdb.service.impl;


import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class GitServiceImplTest {



    @Test
    public void testString() {

        String s = "vuePro";

        System.err.println(s.toLowerCase());

    }



}
