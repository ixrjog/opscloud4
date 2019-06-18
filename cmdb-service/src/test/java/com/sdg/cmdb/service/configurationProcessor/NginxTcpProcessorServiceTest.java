package com.sdg.cmdb.service.configurationProcessor;


import com.sdg.cmdb.dao.cmdb.KubernetesDao;
import com.sdg.cmdb.dao.cmdb.ServerGroupDao;
import com.sdg.cmdb.domain.kubernetes.KubernetesServiceDO;
import com.sdg.cmdb.domain.server.EnvType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class NginxTcpProcessorServiceTest {


    @Autowired
    private NginxTcpProcessorService nginxTcpProcessorService;

    @Resource
    private ServerGroupDao serverGroupDao;

    @Resource
    private KubernetesDao kubernetesDao;


    @Test
    public void testConfig() {
        KubernetesServiceDO kubernetesServiceDO = kubernetesDao.getService(new KubernetesServiceDO(3, "zebra-platform"));
        //  public String getTcpFile(KubernetesServiceDO kubernetesServiceDO, int envType, String portName) {
        String r = nginxTcpProcessorService.getTcpFile(kubernetesServiceDO, EnvType.EnvTypeEnum.test.getCode(), "debug");
        System.err.println(r);
    }


    @Test
    public void testDubboConfig() {
        String r = nginxTcpProcessorService.getTcpDubboFile("k8s-test:test");
        System.err.println(r);
    }

}
