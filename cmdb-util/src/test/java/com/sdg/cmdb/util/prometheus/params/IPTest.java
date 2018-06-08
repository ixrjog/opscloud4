package com.sdg.cmdb.util.prometheus.params;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/11/2.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class IPTest {


    @Test
    public void test() {
        IP ip=new IP("10.100.1.0");
        System.err.println(ip.getIPSection());

        IP ip2=new IP("10.100.1.0/32");
        System.err.println(ip2.getIPSection());

        IP ip3=new IP("10.100.1.0/255.255.255.250");
        System.err.println(ip3.getIPSection());


        IP ip4=new IP("10.100.1.a");
        System.err.println(ip4.getIPSection());


    }

}
