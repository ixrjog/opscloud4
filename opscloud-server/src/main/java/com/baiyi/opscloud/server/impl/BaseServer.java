package com.baiyi.opscloud.server.impl;

import com.baiyi.opscloud.domain.generator.opscloud.OcEnv;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.server.IServer;
import com.baiyi.opscloud.server.facade.ServerAttributeFacade;
import com.baiyi.opscloud.server.factory.ServerFactory;
import com.baiyi.opscloud.service.env.OcEnvService;
import com.baiyi.opscloud.service.server.OcServerService;
import com.google.common.base.Joiner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/4/1 3:41 下午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseServer implements InitializingBean, IServer {

    @Resource
    private ServerAttributeFacade serverAttributeFacade;

    @Resource
    private OcServerService ocServerService;

    @Resource
    private OcEnvService ocEnvService;

    @Override
    public String getKey() {
        return this.getClass().getSimpleName();
    }


    protected String getManageIp(OcServer ocServer) {
        return serverAttributeFacade.getManageIp(ocServer);
    }

    protected List<OcServer> getGroupServerList(int serverGroupId) {
        return ocServerService.queryOcServerByServerGroupId(serverGroupId);
    }

    /**
     * 取主机名称 序号填充对齐
     * 例如 当前主机数量为80台, 第一台序号为 01
     *
     * @param ocServer
     * @return
     */
    protected String getHostName(OcServer ocServer) {
        int serverCount = ocServerService.countByServerGroupId(ocServer.getServerGroupId());
        String format = Joiner.on("").join("%0", String.valueOf(serverCount).length(), "d");
        String sn = String.format(format, ocServer.getSerialNumber());
        return Joiner.on("-").join(acqHostname(ocServer), sn);
    }

    protected String acqHostname(OcServer ocServer) {
        OcEnv ocEnv = ocEnvService.queryOcEnvByType(ocServer.getEnvType());
        if (ocEnv == null || ocEnv.getEnvName().equals("prod")) {
            return ocServer.getName();
        } else {
            return Joiner.on("-").join(ocServer.getName(), ocEnv.getEnvName());
        }
    }

    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        ServerFactory.register(this);
    }
}
