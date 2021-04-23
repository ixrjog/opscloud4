package com.baiyi.opscloud.server;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.BusinessWrapper;
import com.baiyi.opscloud.domain.generator.opscloud.OcCloudServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.param.server.ServerParam;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.facade.ServerBaseFacade;
import com.baiyi.opscloud.facade.ServerFacade;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.cloud.OcCloudServerService;
import com.baiyi.opscloud.service.server.OcServerService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/10 4:28 下午
 * @Version 1.0
 */
public class ServerTest extends BaseUnit {

    @Resource
    private OcServerService ocServerService;


    @Resource
    private OcCloudServerService ocCloudServerService;

    @Resource
    private ServerFacade serverFacade;


    @Test
    void updateCloudServerNameTest() {
        for (OcServer s : ocServerService.queryAllOcServer()) {
            OcCloudServer cloudServer = ocCloudServerService.queryOcCloudServerByUnqueKey(s.getServerType(), s.getId());
            if (cloudServer == null) continue;
            String serverName = ServerBaseFacade.acqServerName(s);
            if (!serverName.equals(cloudServer.getServerName())) {
                System.out.println("原名称 = " + cloudServer.getServerName() + " ; 新名称 = " + serverName);
                cloudServer.setServerName(serverName);
                ocCloudServerService.updateOcCloudServer(cloudServer);
            }
        }
    }

    @Test
    void updateCloudServerNameTest2() {
        OcServer ocServer = ocServerService.queryOcServerByPrivateIp("172.16.202.153");
        OcCloudServer cloudServer = ocCloudServerService.queryOcCloudServerByUnqueKey(ocServer.getServerType(), ocServer.getId());
        if (cloudServer == null) return;
        String serverName = ServerBaseFacade.acqServerName(ocServer);
        if (!serverName.equals(cloudServer.getServerName())) {
            System.err.println("原名称 = " + cloudServer.getServerName() + " ; 新名称 = " + serverName);
            cloudServer.setServerName(serverName);
            ocCloudServerService.updateOcCloudServer(cloudServer);
        }
    }
    
    @Test
    void testJumpserverAssetSync() {
        IServer iServer = ServerFactory.getIServerByKey("JumpserverAsset");
        iServer.sync();
    }


    @Test
    void testZabbixUpdate() {
        IServer iServer = ServerFactory.getIServerByKey("ZabbixHost");
        List<OcServer> ocServerList = ocServerService.queryAllOcServer();
        ocServerList.forEach(ocServer -> iServer.update(ocServer));
//        OcServer ocServer = ocServerService.queryOcServerByIp("172.16.2.222");
//        iServer.update(ocServer);
    }

    @Test
    void test() {
        Integer serialNumber = 0;

//        serialNumber = Integer.valueOf("x");
//        System.err.println(serialNumber);

        // 序号错误
        serialNumber = ocServerService.queryOcServerMaxSerialNumber(44);
        System.err.println(serialNumber);

    }


    @Test
    void test1() {
        List<OcServer> list = ocServerService.queryAllOcServer();
        list.forEach(e -> {
            e.setLoginUser("gegejia");
            ocServerService.updateOcServer(e);
        });

    }

    @Test
    void test2() {
        ServerParam.QueryByServerGroup queryByServerGroup = new ServerParam.QueryByServerGroup();
        // group_opscloud
        queryByServerGroup.setServerGroupId(3);
        BusinessWrapper wrapper = serverFacade.queryServerByServerGroup(queryByServerGroup);
        List<ServerVO.Server> servers = (List<ServerVO.Server>) wrapper.getBody();
        System.err.println(JSON.toJSONString(servers));

    }


    @Test
    void test3() {
        String log = "1111111\n" +
                "2222222\n" +
                "3333333\n" +
                "4444444\n" +
                "5555555\n" +
                "66666666\n" +
                "77777777\n";
        System.err.println(log);

        String r = "";

        int index = 0;
        int max = 3;

        int line = 1;

        while (true) {
            if (line > max) break;

            int find = log.indexOf("\n", index) + 1;
            if (find != 0) {
                index = find;
            } else {
                break;
            }

            line++;
        }
        System.err.println(index);
        System.err.println(log.substring(0, index));
        System.err.println(log.substring(0, index) + "wo shi baiyi");

    }
}
