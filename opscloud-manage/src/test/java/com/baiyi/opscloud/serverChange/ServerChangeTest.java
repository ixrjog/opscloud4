package com.baiyi.opscloud.serverChange;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.common.base.ServerChangeType;
import com.baiyi.opscloud.common.util.UUIDUtils;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerChangeTask;
import com.baiyi.opscloud.domain.param.server.ServerChangeParam;
import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.facade.ServerChangeFacade;
import com.baiyi.opscloud.factory.change.consumer.util.TrySSHUtils;
import com.baiyi.opscloud.factory.change.handler.ServerChangeHandler;
import com.baiyi.opscloud.server.ServerCenter;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.serverChange.OcServerChangeTaskService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/5/29 1:29 下午
 * @Version 1.0
 */
public class ServerChangeTest extends BaseUnit {

    @Resource
    private OcServerService ocServerService;

    @Resource
    private ServerChangeFacade serverChangeFacade;

    @Resource
    private OcServerChangeTaskService ocServerChangeTaskService;

    @Resource
    private ServerChangeHandler serverChangeHandler;

    @Resource
    private ServerCenter serverCenter;

    @Resource
    private KeyboxFacade keyboxFacade;

    @Test
    void testExecuteServerChangeOffline() {
        OcServer ocServer = ocServerService.queryOcServerByIp("192.168.104.190");
        // 192.168.1.108
        ServerChangeParam.ExecuteServerChangeParam param = new ServerChangeParam.ExecuteServerChangeParam();
        param.setChangeType(ServerChangeType.OFFLINE.getType());
        param.setServerGroupId(ocServer.getServerGroupId());
        param.setServerId(ocServer.getId());
        param.setTaskId("TEST-" + UUIDUtils.getUUID());
        serverChangeFacade.executeServerChangeOffline(param);
    }

    @Test
    void testExecuteServerChangeOnline() {
        // 192.168.104.190
        OcServer ocServer = ocServerService.queryOcServerByIp("192.168.104.190");
        ServerChangeParam.ExecuteServerChangeParam param = new ServerChangeParam.ExecuteServerChangeParam();
        param.setChangeType(ServerChangeType.ONLINE.getType());
        param.setServerGroupId(ocServer.getServerGroupId());
        param.setServerId(ocServer.getId());
        param.setTaskId("TEST-" + UUIDUtils.getUUID());
        serverChangeFacade.executeServerChangeOnline(param);
    }


    @Test
    void testServerChangeHandler() {
        String taskId = "4040555cc0334b28a0b852b6d85a2781";
        OcServerChangeTask ocServerChangeTask = ocServerChangeTaskService.queryOcServerChangeTaskByTaskId(taskId);
        serverChangeHandler.executeChangeTask(ocServerChangeTask);
    }

    //         Boolean result = serverCenter.disable(ocServer);
    @Test
    void testServerDisable() {
        OcServer ocServer = ocServerService.queryOcServerByIp("192.168.1.108");
        Boolean result = serverCenter.disable(ocServer);
        System.err.println(result);
    }

    //  10.200.1.34
    @Test
    void testServerTrySSH() {
        OcServer ocServer = ocServerService.queryOcServerByIp("10.200.1.41");
        SSHKeyCredential sshKeyCredential = keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());
        TrySSHUtils.trySSH(ocServer, sshKeyCredential);
    }

}
