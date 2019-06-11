package com.sdg.cmdb.service.sonarTest;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.Measure;
import org.sonar.wsclient.services.Resource;
import org.sonar.wsclient.services.ResourceQuery;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


import java.text.DecimalFormat;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class SonarTest {

    static String host = "http://sonar.ops.yangege.cn";

    static String resourceKey = "org.codehaus.sonar:sonar-ws-client";
    static String[] MEASURES_TO_GET = new String[] { "violations", "lines" };

    @Test
    public void test01() {
        DecimalFormat df = new DecimalFormat("#.##");
        //创建Sonar
      //  Sonar sonar = new Sonar(new HttpClient4Connector(new Host(host, username, password)));
        //执行资源请求
      //  ResourceQuery query = ResourceQuery.createForMetrics(resourceKey, MEASURES_TO_GET);
      //  ResourceQuery query = ResourceQuery.create(resourceKey);
      //  Resource struts = sonar.find(ResourceQuery.createForMetrics("com.ggj.finance:accounting", "coverage", "lines", "violations"));
      //  struts.getMeasure("coverage");

//
//        query.setIncludeTrends(true);
//        Resource resource = sonar.find(query);
//        // 循环遍历获取”violations”, “lines”
//        List<Measure> allMeasures = resource.getMeasures();
//        for (Measure measure : allMeasures) {
//            System.out.println((measure.getMetricKey() + ":"+
//                    df.format(measure.getValue())));
//        }
    }
}
