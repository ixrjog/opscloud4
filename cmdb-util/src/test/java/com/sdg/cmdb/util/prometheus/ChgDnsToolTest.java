package com.sdg.cmdb.util.prometheus;

import com.sdg.cmdb.util.prometheus.params.Dns;
import com.sdg.cmdb.util.prometheus.params.IP;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/19.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ChgDnsToolTest {


    @Test
    public void test() {
        //TODO 这里写用例代码
        String r = "";

        /**
         * 修改dns
         */
        ChgDnsTool cd = new ChgDnsTool(new Dns(new IP("202.101.172.36"),new IP("202.101.172.35")));
        r = cd.getCmdLine();
        System.out.println(r);

        ChgDnsTool cd2 = new ChgDnsTool(new Dns(new IP("202.101.172.36")));
        r = cd2.getCmdLine();
        System.out.println(r);


    }

}
