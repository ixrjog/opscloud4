package com.sdg.cmdb.service;

import com.sdg.cmdb.dao.cmdb.CiDao;
import com.sdg.cmdb.domain.ci.CiDeployStatisticsDO;
import com.sdg.cmdb.service.impl.DingtalkServiceImpl;
import org.apache.commons.lang.text.StrSubstitutor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by liangjian on 2017/4/27.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class DingtalkServiceTest {
    @Resource
    private CiDao ciDao;

    @Resource
    private DingtalkServiceImpl dingtalkServiceImpl;

    @Test
    public void testSendCiDeployMsg() {
        try {
            CiDeployStatisticsDO ciDeployStatisticsDO = ciDao.getCiDeployStatisticsById(375);
            System.err.println(ciDeployStatisticsDO);
            dingtalkServiceImpl.sendCiDeployMsg(ciDeployStatisticsDO);
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("发生错误！");
        }

    }

    @Test
    public void testStr() {
        Map<String, String> valuesMap = new HashMap<String, String>();
        valuesMap.put("str1", "aaaaa");
        valuesMap.put("str2", "bbbbb");
        String templateString =
                "> str1: ${str1}\n" +
                "> str2: ${str2}\n" ;
        StrSubstitutor sub = new StrSubstitutor(valuesMap);
        String resolvedString = sub.replace(templateString);
        System.err.println(resolvedString);
    }


}
