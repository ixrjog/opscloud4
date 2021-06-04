package com.baiyi.caesar.sshcore.handler;

import com.baiyi.caesar.common.base.AccessLevel;
import com.baiyi.caesar.common.type.ProtocolEnum;
import com.baiyi.caesar.common.util.CredentialUtil;
import com.baiyi.caesar.common.util.ServerAccoutUtil;
import com.baiyi.caesar.common.util.SessionUtil;
import com.baiyi.caesar.domain.bo.SshCredential;
import com.baiyi.caesar.domain.generator.caesar.Credential;
import com.baiyi.caesar.domain.generator.caesar.Server;
import com.baiyi.caesar.domain.generator.caesar.ServerAccount;
import com.baiyi.caesar.domain.generator.caesar.UserPermission;
import com.baiyi.caesar.domain.types.BusinessTypeEnum;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.server.ServerAccountService;
import com.baiyi.caesar.service.server.ServerService;
import com.baiyi.caesar.service.sys.CredentialService;
import com.baiyi.caesar.service.user.UserPermissionService;
import com.baiyi.caesar.sshcore.message.BaseMessage;
import com.baiyi.caesar.sshcore.model.HostSystem;
import com.baiyi.caesar.sshcore.model.ServerNode;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

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

    private interface LoginType {
        int LOW_AUTHORITY = 0;
        int HIGH_AUTHORITY = 1;
    }


    public HostSystem buildHostSystem(Server server) {
        SshCredential sshCredential = buildSshCredential(server);
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
            return getSshCredential(server.getId(), baseMessage.getLoginType());
        }
    }

    /**
     * 获取凭据
     *
     * @param serverId
     * @param loginType
     * @return
     */
    private SshCredential getSshCredential(Integer serverId, Integer loginType) {
        UserPermission query = UserPermission.builder()
                .userId(SessionUtil.getUserId())
                .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                .businessId(serverId)
                .build();
        UserPermission userPermission = userPermissionService.getByUserPermission(query);
        if (userPermission == null) return null;
        if ("Admin".equalsIgnoreCase(userPermission.getPermissionRole()))
            return getSshCredentialByAdmin(serverId, loginType);
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(serverId, loginType, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) return null;
        Map<Integer, List<ServerAccount>> accountCatMap = ServerAccoutUtil.catByType(accounts);
        if (accountCatMap.containsKey(loginType)) {
            return buildSshCredential(accountCatMap.get(loginType).get(0));
        }
        return null;
    }

    // 管理员
    private SshCredential getSshCredentialByAdmin(Integer serverId, Integer loginType) {
        List<ServerAccount> accounts = serverAccountService.getPermissionServerAccountByTypeAndProtocol(serverId, null, ProtocolEnum.SSH.getType());
        if (CollectionUtils.isEmpty(accounts)) return null;
        Map<Integer, List<ServerAccount>> accountCatMap = ServerAccoutUtil.catByType(accounts);
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
