package com.sdg.cmdb.util.prometheus;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/18.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class PrometheusUpdateToolTest {


    @Test
    public void test() {
        //TODO 这里写用例代码
        String r = "";
        PrometheusUpdateTool p = new PrometheusUpdateTool();
        r = p.getCmdLine();
        System.out.println(r);
    }
}
