package com.baiyi.opscloud.sshserver.command.custom.kubernetes.base;

import com.baiyi.opscloud.common.datasource.KubernetesConfig;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.service.datasource.DsInstanceAssetService;
import com.baiyi.opscloud.service.datasource.DsInstanceService;
import com.baiyi.opscloud.sshcore.facade.SimpleTerminalSessionFacade;
import com.baiyi.opscloud.sshserver.SshShellHelper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;



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
    private DsConfigManager dsConfigManager;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    protected KubernetesConfig buildConfig(String instanceUuid) {
       return dsConfigManager.buildKubernetesConfig(instanceUuid);
    }

}