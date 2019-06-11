package com.sdg.cmdb.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class RegexUtilsTest {

    @Test
    public void testIsPhone(){
        String phone = "13456788888";
       System.err.println(RegexUtils.isPhone(phone));

    }

    @Test
    public void testIsEmail(){
        String email = "a@bc.com";
        System.err.println(RegexUtils.isEmail(email));
    }

    @Test
    public void testCheckPassword(){
        String password = "1223aaA1";
        System.err.println(RegexUtils.checkPassword(password));
    }

}
