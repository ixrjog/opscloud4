package com.sdg.cmdb.util.prometheus.template.iptables;

import com.sdg.cmdb.util.prometheus.params.IP;
import com.sdg.cmdb.util.prometheus.template.format.IptablesRule;
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
public class IptablesTest {

    @Test
    public void test() {
        Iptables i = new Iptables(buildRules(),"测试规则");
        System.err.println(i.toBody());

    }


    private List<IptablesRule> buildRules() {
        IptablesRule ir = new IptablesRule("80", "HTTP/NGINX");
        List<IP> ipList = new ArrayList<IP>();
        ipList.add(new IP("10.17.0.0/8"));
        IptablesRule ir2 = new IptablesRule(ipList, "内网");
        ipList.add(new IP("192.168.0.0/16"));
        ipList.add(new IP("10.0.0.0/8"));
        IptablesRule ir3 = new IptablesRule(ipList, "多个内网");
        IptablesRule ir4 = new IptablesRule("8080", ipList, "HTTP/TOMCAT");
        IptablesRule ir5 = new IptablesRule("8080:8090", ipList, "HTTP/TOMCAT");

        List<IptablesRule> irList = new ArrayList<IptablesRule>();
        irList.add(ir);
        irList.add(ir2);
        irList.add(ir3);
        irList.add(ir4);
        irList.add(ir5);
        return irList;

    }

}



