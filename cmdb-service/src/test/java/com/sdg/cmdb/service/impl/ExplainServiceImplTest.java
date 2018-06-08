package com.sdg.cmdb.service.impl;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.domain.explain.ExplainDTO;
import com.sdg.cmdb.domain.explain.ExplainInfo;
import com.sdg.cmdb.service.ExplainService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.Arrays;

/**
 * Created by zxxiao on 2017/3/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ExplainServiceImplTest {

    @Resource
    private ExplainService explainService;

    @Test
    public void testdoScanRepoAndTransRemote() {
        ExplainInfo explainInfo = new ExplainInfo();
        explainInfo.setRepo("http://explain@stash.net/scm/cmdb/cmdb.git");
        explainInfo.setScanPath(JSON.toJSONString(Arrays.asList("cmdb/cmdb-service/src/main/resources/sql")));
        explainInfo.setNotifyEmails(JSON.toJSONString(Arrays.asList("xiaozhenxing@net")));

        ExplainDTO explainDTO = new ExplainDTO(explainInfo);

        explainService.doScanRepo(explainDTO);
    }
}
