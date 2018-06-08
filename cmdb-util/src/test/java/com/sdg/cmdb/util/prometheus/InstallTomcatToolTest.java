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
public class InstallTomcatToolTest {

    @Test
    public void test() {
        //TODO 这里写用例代码
        String r="";
        /**
         * 自动安装   -a
         */
        InstallTomcatTool i1=new InstallTomcatTool();
        r=i1.getCmdLine();
        System.out.println(r);

        /**
         * 自动安装,指定项目名  -auto=project
         */
        InstallTomcatTool i2=new InstallTomcatTool("trade");
        r=i2.getCmdLine();
        System.out.println(r);

        /**
         * 指定  Tomcat版本 & 配置文件
         */
        InstallTomcatTool i3=new InstallTomcatTool("7","http://res.51xianqu.net/deploy/trade/tomcat_setenv.conf");
        r=i3.getCmdLine();
        System.out.println(r);
    }

}
