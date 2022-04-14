package com.baiyi.opscloud.sshserver.command.kubernetes.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigHelper;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import lombok.extern.slf4j.Slf4j;
import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2021/7/7 3:52 下午
 * @Version 1.0
 */
@Slf4j
public class BaseKubernetesCommand {

    protected static final int PAGE_FOOTER_SIZE = 6;

    @Resource
    protected SshShellHelper sshShellHelper;

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsConfigHelper dsConfigHelper;

    protected Terminal terminal;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    protected KubernetesConfig buildConfig(String instanceUuid) {
       return dsConfigHelper.buildKubernetesConfig(instanceUuid);
    }

}
