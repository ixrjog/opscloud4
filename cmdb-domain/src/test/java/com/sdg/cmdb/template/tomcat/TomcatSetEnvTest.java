package com.sdg.cmdb.template.tomcat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by zxxiao on 2016/10/12.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class TomcatSetEnvTest {
//    @Test
//    public void test() {
//        //TODO 这里写用例代码
//        TomcatSetenv ts = TomcatSetenv.builder(this.buildData());
//        System.out.println(ts.toBody());
//    }
//
//    private HashMap<String, String> buildData() {
//        HashMap<String, String> data = new HashMap<String, String>();
//        data.put("TOMCAT_APP_NAME_OPT", "trade");
//        data.put("TOMCAT_HTTP_PORT_OPT", "8080");
//        data.put("TOMCAT_SHUTDOWN_PORT_OPT", "8000");
//        data.put("TOMCAT_JMX_rmiRegistryPortPlatform_OPT", "10000");
//        data.put("TOMCAT_JMX_rmiServerPortPlatform_OPT", "10100");
//        data.put("TOMCAT_SERVERXML_WEBAPPSPATH_OPT", "trade");
//        //data.put("HTTP_STATUS_OPT", "webStatus");
//        data.put("APP_CONF_NAME_OPT", "");
//        // data.put("DEL_LOGS", "false");
//        // data.put("BACKUP_WAR", "false");
//        data.put("TOMCAT_INSTALL_PATH", "/usr/local");
//        data.put("TOMCAT_HTTP_URI_ENCODING", "utf8");
//        data.put("OPEN_TOMCAT_JAVA_OPTS", "false");
//        data.put("TOMCAT_JAVA_OPTS", "-");
//        data.put("HTTP_STATUS_TIME", "5");
//        data.put("SET_JVM_Xss", "256k");
//        data.put("SET_JVM_Xms", "");
//        data.put("SET_JVM_Xmx", "");
//        data.put("SET_JVM_Xmn", "");
//        return data;
//    }

}
