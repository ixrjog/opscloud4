package com.baiyi.opscloud.sshserver.command.kubernetes.base;

import com.baiyi.opscloud.common.datasource.KubernetesDsInstanceConfig;
import com.baiyi.opscloud.datasource.factory.DsConfigFactory;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceConfig;
import com.baiyi.opscloud.domain.generator.opscloud.DatasourceInstance;
import com.baiyi.opscloud.service.datasource.DsConfigService;
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
    protected SshShellHelper helper;

    @Resource
    protected DsInstanceService dsInstanceService;

    @Resource
    protected DsInstanceAssetService dsInstanceAssetService;

    @Resource
    private DsConfigService dsConfigService;

    @Resource
    private DsConfigFactory dsFactory;

    protected Terminal terminal;

    @Resource
    protected SimpleTerminalSessionFacade simpleTerminalSessionFacade;

    @Autowired
    @Lazy
    public void setTerminal(Terminal terminal) {
        this.terminal = terminal;
    }

    protected KubernetesDsInstanceConfig buildConfig(String instanceUuid) {
        DatasourceInstance instance = dsInstanceService.getByUuid(instanceUuid);
        DatasourceConfig datasourceConfig = dsConfigService.getById(instance.getId());
        return dsFactory.build(datasourceConfig, KubernetesDsInstanceConfig.class);
    }

}
