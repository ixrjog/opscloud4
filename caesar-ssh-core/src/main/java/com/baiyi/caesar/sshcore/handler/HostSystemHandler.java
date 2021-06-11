package com.baiyi.caesar.sshcore.handler;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.exception.ssh.SshRuntimeException;
import com.baiyi.caesar.common.type.ProtocolEnum;
import com.baiyi.caesar.common.util.CredentialUtil;
import com.baiyi.caesar.common.util.ServerAccoutUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.ErrorEnum;
import com.baiyi.caesar.domain.bo.SshCredential;
import com.baiyi.caesar.domain.generator.caesar.*;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.server.ServerAccountService;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.service.sys.CredentialService;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.service.user.UserService;
import com.baiyi.caesar.sshcore.account.SshAccount;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.model.HostSystem;
import com.baiyi.caesar.sshcore.model.ServerNode;
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
    private AuthRoleService authRoleService;

    @Resource
    private SshAccount sshAccount;

    @Resource
    private UserService userService;

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }

//    public HostSystem buildHostSystem(Server server) {
//        SshCredential sshCredential = buildSshCredential(server);
//        return HostSystem.builder()
//                .host(server.getPrivateIp()) // 避免绕过未授权服务器
//                .sshCredential(sshCredential)
//                .build();
//    }

    public HostSystem buildHostSystem(Server server, String username, String account) throws SshRuntimeException {
        User user = userService.getByUsername(username);
        SessionUtil.setUsername(username);
        SessionUtil.setUserId(user.getId());
        UserPermission query = UserPermission.builder()
                .userId(SessionUtil.getUserId())
                .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                .businessId(server.getServerGroupId())
                .build();
        UserPermission userPermission = userPermissionService.getByUserPermission(query);
        if (userPermission == null)
            throw new SshRuntimeException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILUER);
        SshCredential sshCredential = null;
        if (StringUtils.isEmpty(account)) {
            sshCredential = getSshCredential(server, LoginType.LOW_AUTHORITY);
            if (sshCredential == null && "admin".equalsIgnoreCase(userPermission.getPermissionRole()))
                sshCredential = getSshCredential(server, LoginType.HIGH_AUTHORITY);
        } else {
            ServerAccount serverAccount = serverAccountService.getPermissionServerAccountByUsernameAndProtocol(server.getId(), account, ProtocolEnum.SSH.getType());
            if (serverAccount == null)
                throw new SshRuntimeException(ErrorEnum.SSH_SERVER_ACCOUNT_NOT_EXIST);
            if (serverAccount.getAccountType() == 1 && !"admin".equalsIgnoreCase(userPermission.getPermissionRole()))
                throw new SshRuntimeException(ErrorEnum.SSH_SERVER_AUTHENTICATION_FAILUER);
            sshCredential = buildSshCredential(serverAccount);
        }
        if (sshCredential == null)
            throw new SshRuntimeException(ErrorEnum.SSH_SERVER_NO_ACCOUNTS_AVAILABLE);
        return HostSystem.builder()
                .host(server.getPrivateIp()) // 避免绕过未授权服务器
                .sshCredential(sshCredential)
                .build();
    }

    public HostSystem buildHostSystem(ServerNode serverNode, BaseMessage baseMessage) {
        baseMessage.setAdmin(isAdmin());
        Server server = serverService.getById(serverNode.getId());
        SshCredential sshCredential = buildSshCredential(baseMessage, server);
        return HostSystem.builder()
                .host(server.getPrivateIp()) // 避免绕过未授权服务器
                .sshCredential(sshCredential)
                .loginMessage(baseMessage)
                .build();
    }

    /**
     * OPS角色以上即认定为系统管理员
     *
     * @return
     */
    private boolean isAdmin() {
        int accessLevel = authRoleService.getRoleAccessLevelByUsername(SessionUtil.getUsername());
        return accessLevel >= AccessLevel.OPS.getLevel();
    }


    private SshCredential buildSshCredential(Server server) {
        return getSshCredentialByAdmin(server.getId(), 1);
    }

    private SshCredential buildSshCredential(BaseMessage baseMessage, Server server) {
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
    private SshCredential getSshCredential(Server server, Integer loginType) {
        UserPermission query = UserPermission.builder()
                .userId(SessionUtil.getUserId())
                .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                .businessId(server.getServerGroupId())
                .build();
        UserPermission userPermission = userPermissionService.getByUserPermission(query);
        if (userPermission == null) return null;
        if ("Admin".equalsIgnoreCase(userPermission.getPermissionRole()))
            return getSshCredentialByAdmin(server.getId(), loginType);
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(server.getId(), loginType, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) return null;
        Map<Integer, List<ServerAccount>> accountCatMap = ServerAccoutUtil.catByType(accounts);
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).get(0));
        }
        return null;
    }

    // 管理员
    private SshCredential getSshCredentialByAdmin(Integer serverId, Integer loginType) {
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
