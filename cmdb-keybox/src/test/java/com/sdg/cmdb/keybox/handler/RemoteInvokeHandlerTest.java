package com.sdg.cmdb.keybox.handler;

import com.alibaba.fastjson.JSON;
import com.sdg.cmdb.dao.cmdb.KeyboxDao;
import com.sdg.cmdb.domain.HttpResult;
import com.sdg.cmdb.domain.keybox.HostSystem;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * Created by zxxiao on 2016/11/4.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:springtest/context.xml"})
public class RemoteInvokeHandlerTest {

    @Resource
    private KeyboxDao keyboxDao;

    @Test
    public void testRemoteInvoke() {
        HostSystem hostSystem = new HostSystem();
        hostSystem.setInstanceId("1");
        hostSystem.setHost("10.17.1.120");
        hostSystem.setPort(22);
        hostSystem.setUser("manage");

        for(int i = 0; i < 10; i++) {
            ConnectionSession connectionSession = RemoteInvokeHandler.getSession(keyboxDao.getApplicationKey(), hostSystem);

            if (connectionSession != null) {
                String uniqueKey = "aaa-" + i;
                connectionSession.registerRead(uniqueKey, new SessionBridge() {
                    @Override
                    public void process(String value) {
                        HttpResult result = new HttpResult(value);
                        System.err.println(JSON.toJSONString(result));
                    }
                });
                connectionSession.unregisterRead(uniqueKey);
                connectionSession.close();
            }
        }
    }

    @Test
    public void testRemoteInvoke2() {
        HostSystem hostSystem = new HostSystem();
        hostSystem.setInstanceId("1");
        hostSystem.setHost("10.17.1.120");
        hostSystem.setPort(22);
        hostSystem.setUser("manage");

        ConnectionSession connectionSession = RemoteInvokeHandler.getSession(keyboxDao.getApplicationKey(), hostSystem);

        connectionSession.registerRead("aaa", new SessionBridge() {
            @Override
            public void process(String value) {
                HttpResult result = new HttpResult(value);
                System.err.println(JSON.toJSONString(result));
            }
        });


        try {
            connectionSession.writeToRemote("sudo bash\n");
            //Thread.sleep(500);
            //connectionSession.writeToRemote("ps -ef\n");
            //Thread.sleep(500);
            connectionSession.writeToRemote("prometheus_update\n");
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
