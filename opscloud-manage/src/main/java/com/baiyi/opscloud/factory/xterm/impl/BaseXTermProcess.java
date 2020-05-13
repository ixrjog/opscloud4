package com.baiyi.opscloud.factory.xterm.impl;

import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.domain.bo.SSHKeyCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcServer;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserPermission;
import com.baiyi.opscloud.facade.KeyboxFacade;
import com.baiyi.opscloud.facade.OcAuthFacade;
import com.baiyi.opscloud.facade.ServerGroupFacade;
import com.baiyi.opscloud.factory.xterm.IXTermProcess;
import com.baiyi.opscloud.factory.xterm.XTermProcessFactory;
import com.baiyi.opscloud.service.server.OcServerService;
import com.baiyi.opscloud.service.user.OcUserPermissionService;
import com.baiyi.opscloud.service.user.OcUserService;
import com.baiyi.opscloud.xterm.message.BaseXTermWSMessage;
import com.baiyi.opscloud.xterm.model.HostSystem;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import javax.annotation.Resource;

import static com.baiyi.opscloud.factory.xterm.impl.XTermInitialProcess.HIGH_AUTHORITY_ACCOUNT;

/**
 * @Author baiyi
 * @Date 2020/5/11 9:35 上午
 * @Version 1.0
 */
@Slf4j
public abstract class BaseXTermProcess implements IXTermProcess, InitializingBean {

    @Resource
    protected OcAuthFacade ocAuthFacade;

    @Resource
    protected OcUserService ocUserService;

    @Resource
    protected ServerGroupFacade serverGroupFacade;

    @Resource
    protected KeyboxFacade keyboxFacade;

    @Resource
    protected OcServerService ocServerService;

    @Resource
    protected OcUserPermissionService ocUserPermissionService;

  //  protected static final String sessionId = UUID.randomUUID().toString();

    abstract protected BaseXTermWSMessage getXTermMessage(String message);

    protected HostSystem buildHostSystem(OcUser ocUser, String host, BaseXTermWSMessage baseMessage) {
        OcServer ocServer = ocServerService.queryOcServerByIp(host);
        OcUserPermission ocUserPermission = new OcUserPermission();
        ocUserPermission.setUserId(ocUser.getId());
        ocUserPermission.setBusinessId(ocServer.getServerGroupId());
        ocUserPermission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());

        boolean loginType = false;
        if (baseMessage.getLoginUserType() == 1) {
            OcUserPermission checkUserPermission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(ocUserPermission);
            if (checkUserPermission != null)
                loginType = true;
        }

        SSHKeyCredential sshKeyCredential;
        if (loginType) {
            sshKeyCredential = keyboxFacade.getSSHKeyCredential(HIGH_AUTHORITY_ACCOUNT); // 高权限
        } else {
            sshKeyCredential = keyboxFacade.getSSHKeyCredential(ocServer.getLoginUser());  // 普通用户
        }
        HostSystem hostSystem = new HostSystem();
        hostSystem.setHost(host);
        hostSystem.setSshKeyCredential(sshKeyCredential);
        hostSystem.setInitialMessage(baseMessage);

        return hostSystem;
    }


    /**
     * 注册
     *
     * @throws Exception
     */
    @Override
    public void afterPropertiesSet() throws Exception {
        XTermProcessFactory.register(this);
    }

}
