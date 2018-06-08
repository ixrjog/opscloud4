package com.sdg.cmdb.util.prometheus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/17.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class AnsibleToolTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        String r ="";
        /**
         * String host_group, boolean isSudo, int ansible_forks, String cmdType, String cmd
         */
        AnsibleTool a1= new AnsibleTool("trade-gray",true,1,"-a","install_java -a -b -c");
        r=a1.getCmdLine();
        System.out.println(r);


        /**
         * String host_group, int ansible_forks, String cmd
         */
        AnsibleTool a2= new AnsibleTool("trade-gray",1,"install_tomcat -a -b -c");
        r=a2.getCmdLine();
        System.out.println(r);
    }

}
