package com.sdg.cmdb.util.prometheus.template.format;

import com.sdg.cmdb.util.prometheus.params.IP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liangjian on 16/11/2.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class IptablesRuleTest {

    @Test
    public void test() {
        IptablesRule ir = new IptablesRule("80", "HTTP/NGINX");
        System.err.println(ir.getRule());

        List<IP> ipList = new ArrayList<IP>();
        ipList.add(new IP("10.17.0.0/8"));
        IptablesRule ir2 = new IptablesRule(ipList, "内网");
        System.err.println(ir2.getRule());

        ipList.add(new IP("192.168.0.0/16"));
        ipList.add(new IP("10.0.0.0/8"));
        IptablesRule ir3 = new IptablesRule(ipList, "多个内网");
        System.err.println(ir3.getRule());


        IptablesRule ir4 = new IptablesRule("8080",ipList,"HTTP/TOMCAT");
        System.err.println(ir4.getRule());
        IptablesRule ir5 = new IptablesRule("8080:8090",ipList,"HTTP/TOMCAT");
        System.err.println(ir5.getRule());


    }

}
