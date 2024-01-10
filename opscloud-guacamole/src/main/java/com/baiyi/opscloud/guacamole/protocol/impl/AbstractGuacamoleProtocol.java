package com.baiyi.opscloud.guacamole.protocol.impl;

import com.baiyi.opscloud.common.constants.enums.DsTypeEnum;
import com.baiyi.opscloud.common.datasource.GuacamoleConfig;
import com.baiyi.opscloud.common.util.RandomUtil;
import com.baiyi.opscloud.core.InstanceHelper;
import com.baiyi.opscloud.core.factory.DsConfigManager;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.param.guacamole.GuacamoleParam;
import com.baiyi.opscloud.guacamole.protocol.GuacamoleProtocolFactory;
import com.baiyi.opscloud.guacamole.protocol.IGuacamoleProtocol;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.CredentialService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.guacamole.GuacamoleException;
import org.apache.guacamole.net.GuacamoleSocket;
import org.apache.guacamole.net.InetGuacamoleSocket;
import org.apache.guacamole.protocol.ConfiguredGuacamoleSocket;
import org.apache.guacamole.protocol.GuacamoleConfiguration;
import org.jasypt.encryption.StringEncryptor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/7/9 1:46 下午
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractGuacamoleProtocol implements IGuacamoleProtocol, InitializingBean {

    @Resource
    private InstanceHelper instanceHelper;

    @Resource
    private DsConfigManager dsConfigManager;

    @Resource
    private CredentialService credentialService;

    @Resource
    private StringEncryptor stringEncryptor;

    @Resource
    private ServerService serverService;

    @Resource
    private ServerAccountService serverAccountService;

    @Resource
    private BizPropertyHelper businessPropertyHelper;

    /**
     * 支持认证的实例类型
     */
    private static final DsTypeEnum[] FILTER_INSTANCE_TYPES = {DsTypeEnum.GUACAMOLE};

    private DsTypeEnum[] getFilterInstanceTypes() {
        return FILTER_INSTANCE_TYPES;
    }

    protected abstract Map<String, String> buildParameters(Server server, ServerAccount serverAccount, GuacamoleParam.Login guacamoleLogin);

    protected Credential getCredential(ServerAccount serverAccount) {
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        if (StringUtils.isEmpty(serverAccount.getUsername())) {
            serverAccount.setUsername(credential.getUsername());
        }
        credential.setCredential(stringEncryptor.decrypt(credential.getCredential()));
        return credential;
    }

    @Override
    public GuacamoleSocket buildGuacamoleSocket(GuacamoleParam.Login guacamoleLogin) throws GuacamoleException {
        GuacamoleConfiguration configuration = new GuacamoleConfiguration();
        configuration.setProtocol(getProtocol()); // 协议
        Server server = serverService.getById(guacamoleLogin.getServerId());
        ServerAccount serverAccount = serverAccountService.getById(guacamoleLogin.getServerAccountId());
        configuration.setParameters(buildParameters(server, serverAccount, guacamoleLogin));
        GuacamoleConfig guacamoleConfig = getConfig();
        return new ConfiguredGuacamoleSocket(
                new InetGuacamoleSocket(guacamoleConfig.getGuacamole().getHost(), guacamoleConfig.getGuacamole().getPort()),
                configuration
        );
    }

    private GuacamoleConfig getConfig() throws GuacamoleException{
        List<DatasourceInstance> instances = instanceHelper.listInstance(getFilterInstanceTypes(), getProtocol());
        if (CollectionUtils.isEmpty(instances)) {
            throw new GuacamoleException("无可用的Guacamole数据源实例！");
        }
        int index = RandomUtil.random(instances.size());
        DatasourceInstance instance = instances.get(index);
        DatasourceConfig datasourceConfig = dsConfigManager.getConfigById(instance.getConfigId());

       return dsConfigManager.build(datasourceConfig, GuacamoleConfig.class);
    }

    protected ServerProperty.Server getBusinessProperty(Server server) {
        return businessPropertyHelper.getBusinessProperty(server);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        GuacamoleProtocolFactory.register(this);
    }

}