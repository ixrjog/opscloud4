package com.sdg.cmdb.template.format.configurationitem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by liangjian on 16/10/13.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class JavaProjectsOptTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        JavaProjectsOpt jpo = new JavaProjectsOpt();

        jpo.put(JavaProjectsOpt.buider("logistics","logistics","gray:production"));
        jpo.put(JavaProjectsOpt.buider("logistics-job","logistics-job","gray:production"));
        jpo.put(JavaProjectsOpt.buider("logistics-backstage","logistics-backstage","gray:production"));
        jpo.put(JavaProjectsOpt.buider("trade","logistics-job","gray:production"));
        jpo.put(JavaProjectsOpt.buider("logistics-job","logistics-job","gray:production"));

        System.out.println(jpo.toBody());
    }

}
