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
public class ChgHostnameToolTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        String r = "";

        /**
         * hostname
         */
        ChgHostnameTool c1 = new ChgHostnameTool("trade-gray");
        r = c1.getCmdLine();
        System.out.println(r);


        /*
        hostname ,  ip
         */
        ChgHostnameTool c2 = new ChgHostnameTool("trade-daily", "10.17.1.220");
        r = c2.getCmdLine();
        System.out.println(r);

    }

}
