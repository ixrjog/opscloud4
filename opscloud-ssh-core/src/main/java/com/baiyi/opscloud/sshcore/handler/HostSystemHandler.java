package com.baiyi.opscloud.sshcore.handler;

import com.baiyi.opscloud.common.exception.ssh.SshRuntimeException;
import com.baiyi.opscloud.common.type.ProtocolEnum;
import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.common.util.CredentialUtil;
import com.baiyi.opscloud.common.util.ServerAccountUtil;
import com.baiyi.opscloud.common.util.SessionUtil;
import com.baiyi.opscloud.domain.ErrorEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Credential;
import com.baiyi.opscloud.domain.generator.opscloud.Server;
import com.baiyi.opscloud.domain.generator.opscloud.ServerAccount;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.model.SshCredential;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.domain.vo.server.ServerVO;
import com.baiyi.opscloud.service.server.ServerAccountService;
import com.baiyi.opscloud.service.server.ServerService;
import com.baiyi.opscloud.service.sys.CredentialService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.sshcore.account.SshAccount;
import com.baiyi.opscloud.sshcore.message.server.BaseServerMessage;
import com.baiyi.opscloud.sshcore.model.HostSystem;
import com.baiyi.opscloud.sshcore.model.ServerNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Author baiyi
 * @Date 2021/6/3 10:45 上午
 * @Version 1.0
 */
@Component
public class HostSystemHandler {

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private ServerAccountService serverAccountService;

    @Resource
    private ServerService serverService;

    @Resource
    private CredentialService credentialService;

    @Resource
    private CredentialUtil credentialUtil;

    @Resource
    private SshAccount sshAccount;

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

    private boolean checkAdmin(Server server) {
        boolean isAdmin = SessionUtil.getIsAdmin();
        if (!isAdmin) {
            UserPermission query = UserPermission.builder()
                    .userId(SessionUtil.getUserId())
                    .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                    .businessId(server.getServerGroupId())
                    .build();
            UserPermission userPermission = userPermissionService.getByUserPermission(query);
            if (userPermission == null)
                throw new SshRuntimeException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILURE);
            isAdmin = "admin".equalsIgnoreCase(userPermission.getPermissionRole());
        }
        return isAdmin;
    }

    public HostSystem buildHostSystem(ServerVO.Server serverVO, String account) throws SshRuntimeException {
        return buildHostSystem(BeanCopierUtil.copyProperties(serverVO, Server.class), account);
    }

    public HostSystem buildHostSystem(Server server, String account) throws SshRuntimeException {
        boolean isAdmin = checkAdmin(server);

        SshCredential sshCredential;
        if (StringUtils.isEmpty(account)) {
            sshCredential = getSshCredential(server, LoginType.LOW_AUTHORITY);
            if (sshCredential == null && isAdmin)
                sshCredential = getSshCredential(server, LoginType.HIGH_AUTHORITY);
        } else {
            ServerAccount serverAccount = serverAccountService.getPermissionServerAccountByUsernameAndProtocol(server.getId(), account, ProtocolEnum.SSH.getType());
            if (serverAccount == null)
                throw new SshRuntimeException(ErrorEnum.SSH_SERVER_ACCOUNT_NOT_EXIST);
            if (serverAccount.getAccountType() == LoginType.HIGH_AUTHORITY && !isAdmin)
                throw new SshRuntimeException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILURE);
            sshCredential = buildSshCredential(serverAccount);
        }
        if (sshCredential == null)
            throw new SshRuntimeException(ErrorEnum.SSH_SERVER_NO_ACCOUNTS_AVAILABLE);
        return HostSystem.builder()
                .host(server.getPrivateIp()) // 避免绕过未授权服务器
                .sshCredential(sshCredential)
                .build();
    }

    public HostSystem buildHostSystem(ServerNode serverNode, BaseServerMessage message) {
        message.setAdmin(SessionUtil.getIsAdmin());
        Server server = serverService.getById(serverNode.getId());
        SshCredential sshCredential = buildSshCredential(message, server);
        return HostSystem.builder()
                .host(server.getPrivateIp()) // 避免绕过未授权服务器
                .sshCredential(sshCredential)
                .loginMessage(message)
                .build();
    }

    private SshCredential buildSshCredential(BaseServerMessage baseMessage, Server server) {
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
        if (SessionUtil.getIsAdmin())
            return getSshCredentialByAdmin(server.getId(), loginType);
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(server.getId(), loginType, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) return null;
        Map<Integer, List<ServerAccount>> accountCatMap = ServerAccountUtil.catByType(accounts);
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).get(0));
        }
        return null;
    }

    // 管理员
    private SshCredential getSshCredentialByAdmin(Integer serverId, int loginType) {
        Map<Integer, List<ServerAccount>> accountCatMap = sshAccount.getServerAccountCatMap(serverId);
        if (accountCatMap == null) return null;
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).get(0));
        }
        if (loginType == LoginType.LOW_AUTHORITY) {
            if (accountCatMap.containsKey(LoginType.HIGH_AUTHORITY)) {
                return buildSshCredential(accountCatMap.get(LoginType.HIGH_AUTHORITY).get(0));
            }
        }
        return null; // 未找到凭据
    }

    private SshCredential buildSshCredential(ServerAccount serverAccount) {
        Credential credential = credentialService.getById(serverAccount.getCredentialId());
        credentialUtil.decrypt(credential);       // 解密
        return SshCredential.builder()
                .serverAccount(serverAccount)
                .credential(credential)
                .build();
    }
}
