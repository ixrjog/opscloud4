package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class UUIDUtilsTest {

    @Test
    public void test(){
       System.err.println(UUIDUtils.getUUID());

        System.err.println(UUIDUtils.getUUIDByShort());
        //f96084741c084c4388d6e330b1afc019
        String uuid ="0a9139f3-2579-4783-895b-091f9d721c64";
        String short_uuid = "0a9139f325794783895b091f9d721c64";
        System.err.println(uuid)  ;

       System.err.println(UUIDUtils.convertUUID(uuid));
    }
}
