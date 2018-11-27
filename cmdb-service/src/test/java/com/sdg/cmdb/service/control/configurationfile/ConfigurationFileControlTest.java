package com.sdg.cmdb.service.control.configurationfile;

import com.sdg.cmdb.domain.server.ServerDO;
import com.sdg.cmdb.domain.server.ServerGroupDO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * Created by liangjian on 2016/12/15.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class ConfigurationFileControlTest {

    @Resource
    private AnsibleHostService ansibleHostService;

    @Resource
    private InterfaceCIService interfaceCIService;

    @Resource
    private TomcatInstallConfigService tomcatInstallConfigService;

    @Resource
    private ConfigurationFileControlService configurationFileControlService;

    @Resource
    private ShadowsocksService shadowsocksService;

    @Resource
    private GetwayService getwayService;



    @Test
    public void testConfigurationFileControlService() {
        System.err.println(configurationFileControlService.build(ConfigurationFileControlService.nameAnsible, 0));

        System.err.println(configurationFileControlService.build(ConfigurationFileControlService.nameInterfaceCI, 0));

        System.err.println(configurationFileControlService.build(ConfigurationFileControlService.nameTomcatInstallConfig, 0));

        System.err.println(configurationFileControlService.build(ConfigurationFileControlService.nameShadowsocks, 0));

    }

    @Test
    public void testShadowsocksService() {
        //// TODO 生成翻墙配置
        String result = shadowsocksService.acqConfig();
        System.err.println(result);
    }

    @Test
    public void testAnsibleHostService() {
        //// TODO ansible 只分2组列表
        String result =  ansibleHostService.acqHostsAllCfg();
        System.err.println(result);
    }

    @Test
    public void testAnsibleHostService2() {
        //// TODO ansible 全局列表
         String result =  ansibleHostService.acqHostsCfgByUseType(1);
         System.err.println(result);
    }

    @Test
    public void testInterfaceCIService() {
        //// TODO 持续集成接口
        String result = interfaceCIService.acqConfig();
        System.err.println(result);
    }

    @Test
    public void testTomcatInstallConfigService() {
        //// TODO tomcat安装资源配置
        String result = tomcatInstallConfigService.acqConfig();
        System.err.println(result);
    }

    @Test
    public void testGetwayService() {
        //// TODO getway全局列表
        String result = getwayService.acqHostConfig();
        System.err.println(result);
    }



    @Test
    public void testNginxLocationKaServiceDaily() {
        //// TODO ngin-ka-upstream（daily）配置
//        String result = nginxLocationKaService.acqConfig(ServerDO.EnvTypeEnum.daily.getCode());
//        System.err.println(result);
    }

    @Test
    public void testNginxLocationKaServiceGray() {
        //// TODO ngin-ka-upstream（gray）配置
//        String result = nginxLocationKaService.acqConfig(ServerDO.EnvTypeEnum.gray.getCode());
//        System.err.println(result);
    }

    @Test
    public void testNginxLocationKaServiceProd() {
        //// TODO ngin-ka-upstream（prod）配置
//        String result = nginxLocationKaService.acqConfig(ServerDO.EnvTypeEnum.prod.getCode());
//        System.err.println(result);
    }






    @Test
    public void testNginxLocationSupplierServiceDaily() {
        //// TODO ngin-supplier-location（daily）配置
        //String result = nginxLocationSupplierService.acqConfig(ServerDO.EnvTypeEnum.daily.getCode());
        //System.err.println(result);

        String result = configurationFileControlService.build(ConfigurationFileControlService.nameNginxLocationSupplier,2);
        System.err.println(result);
    }

}
