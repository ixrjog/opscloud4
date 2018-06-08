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
public class InstallJavaToolTest {


    @Test
    public void test() {
        //TODO 这里写用例代码
        String r="";

        /**
         * 默认 version 7
         */
        InstallJavaTool j1=new InstallJavaTool();
        r=j1.getCmdLine();
        System.out.println(r);


        /*
        指定版本
         */
        InstallJavaTool j2=new InstallJavaTool("8");
        r=j2.getCmdLine();
        System.out.println(r);

    }
}
