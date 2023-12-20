package com.baiyi.opscloud.sshcore.handler;

import com.baiyi.opscloud.common.constants.enums.ProtocolEnum;
import com.baiyi.opscloud.common.exception.ssh.SshException;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.CredentialUtil;
import com.baiyi.opscloud.common.util.ServerAccountUtil;
import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.datasource.business.server.util.HostParamUtil;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.model.SshCredential;
import com.baiyi.opscloud.domain.model.property.ServerProperty;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.business.BizPropertyHelper;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.CredentialService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.sshcore.SshAccountHelper;
import com.baiyi.opscloud.sshcore.message.ServerMessage;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.ServerNode;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/3 10:45 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class HostSystemHandler {

    private final UserPermissionService userPermissionService;

    private final ServerAccountService serverAccountService;

    private final ServerService serverService;

    private final CredentialService credentialService;

    private final CredentialUtil credentialUtil;

    private final SshAccountHelper sshAccountHelper;

    private final BizPropertyHelper bizPropertyHelper;

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    private boolean verifyAdmin(Server server) {
        boolean isAdmin = SessionHolder.getIsAdmin();
        if (!isAdmin) {
            UserPermission query = UserPermission.builder()
                    .userId(SessionHolder.getUserId())
                    .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                    .businessId(server.getServerGroupId())
                    .build();
            UserPermission userPermission = userPermissionService.getByUserPermission(query);
            if (userPermission == null) {
                throw new SshException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILURE);
            }
            isAdmin = "admin".equalsIgnoreCase(userPermission.getPermissionRole());
        }
        return isAdmin;
    }

    public HostSystem buildHostSystem(ServerVO.Server serverVO, String account, boolean admin) throws SshException {
        return buildHostSystem(BeanCopierUtil.copyProperties(serverVO, Server.class), account, admin);
    }

    public HostSystem buildHostSystem(Server server, String account, boolean admin) throws SshException {
        boolean isAdmin = verifyAdmin(server);
        SshCredential sshCredential;
        // 未指定账户
        if (StringUtils.isEmpty(account)) {
            if (admin && isAdmin) {
                sshCredential = getSshCredential(server, LoginType.HIGH_AUTHORITY);
            } else {
                sshCredential = getSshCredential(server, LoginType.LOW_AUTHORITY);
                if (sshCredential == null && isAdmin) {
                    sshCredential = getSshCredential(server, LoginType.HIGH_AUTHORITY);
                }
            }
        } else { // 指定账户
            ServerAccount serverAccount = serverAccountService.getPermissionServerAccountByUsernameAndProtocol(server.getId(), account, ProtocolEnum.SSH.getType());
            if (serverAccount == null) {
                throw new SshException(ErrorEnum.SSH_SERVER_ACCOUNT_NOT_EXIST);
            }
            if (serverAccount.getAccountType() == LoginType.HIGH_AUTHORITY && !isAdmin) {
                throw new SshException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILURE);
            }
            sshCredential = buildSshCredential(serverAccount);
        }
        if (sshCredential == null) {
            throw new SshException(ErrorEnum.SSH_SERVER_NO_ACCOUNTS_AVAILABLE);
        }
        ServerProperty.Server serverProperty = bizPropertyHelper.getBusinessProperty(server);
        return HostSystem.builder()
                // 避免绕过未授权服务器
                .host(HostParamUtil.getManageIp(server, serverProperty))
                .port(HostParamUtil.getSshPort(serverProperty))
                .sshCredential(sshCredential)
                .build();
    }

    public HostSystem buildHostSystem(ServerNode serverNode, ServerMessage.BaseMessage message) {
        message.setAdmin(SessionHolder.getIsAdmin());
        Server server = serverService.getById(serverNode.getId());
        SshCredential sshCredential = buildSshCredential(message, server);
        ServerProperty.Server serverProperty = bizPropertyHelper.getBusinessProperty(server);
        return HostSystem.builder()
                // 避免绕过未授权服务器
                .host(HostParamUtil.getManageIp(server, serverProperty))
                .port(HostParamUtil.getSshPort(serverProperty))
                .sshCredential(sshCredential)
                .loginMessage(message)
                .build();
    }

    private SshCredential buildSshCredential(ServerMessage.BaseMessage baseMessage, Server server) {
        if (baseMessage.isAdmin()) {
            return getSshCredentialByAdmin(server.getId(), baseMessage.getLoginType());
        } else {
            return getSshCredential(server, baseMessage.getLoginType());
        }
    }

    /**
     * 获取凭据
     *
     * @param server
     * @param loginType
     * @return
     */
    private SshCredential getSshCredential(Server server, int loginType) {
        if (SessionHolder.getIsAdmin()) {
            return getSshCredentialByAdmin(server.getId(), loginType);
        }
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(server.getId(), loginType, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) {
            return null;
        }
        Map<Integer, List<ServerAccount>> accountCatMap = ServerAccountUtil.catByType(accounts);
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).getFirst());
        }
        return null;
    }

    /**
     * 管理员
     *
     * @param serverId
     * @param loginType
     * @return
     */
    private SshCredential getSshCredentialByAdmin(Integer serverId, int loginType) {
        Map<Integer, List<ServerAccount>> accountCatMap = sshAccountHelper.getServerAccountCatMap(serverId);
        if (accountCatMap == null) {
            return null;
        }
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).getFirst());
        }
        if (loginType == LoginType.LOW_AUTHORITY) {
            if (accountCatMap.containsKey(LoginType.HIGH_AUTHORITY)) {
                return buildSshCredential(accountCatMap.get(LoginType.HIGH_AUTHORITY).getFirst());
            }
        }
        // 未找到凭据
        return null;
    }

    private SshCredential buildSshCredential(ServerAccount serverAccount) {
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        // 解密
        credentialUtil.decrypt(credential);
        return SshCredential.builder()
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }

}