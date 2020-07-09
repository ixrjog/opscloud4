package com.baiyi.opscloud.decorator.user;

import com.baiyi.opscloud.common.base.AccountType;
import com.baiyi.opscloud.common.base.BusinessType;
import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.*;
import com.baiyi.opscloud.domain.vo.server.ServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.UserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.facade.AliyunRAMFacade;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.user.*;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2020/2/25 12:06 下午
 * @Version 1.0
 */
@Component("UserDecorator")
public class UserDecorator {

    @Resource
    private OcUserGroupService ocUserGroupService;

    @Resource
    private OcServerGroupService ocServerGroupService;

    @Resource
    private OcUserApiTokenService ocUserApiTokenService;

    @Resource
    private OcUserCredentialService ocUserCredentialService;

    @Resource
    private JumpserverCenter jumpserverCenter;

    @Resource
    private OcUserPermissionService ocUserPermissionService;

    @Resource
    private OcAccountService ocAccountService;

    @Resource
    private PersonRepo personRepo;

    @Resource
    private AliyunRAMFacade aliyunRAMFacade;

    // from mysql
    public UserVO.User decorator(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            // 装饰 用户组
            List<OcUserGroup> userGroupList = ocUserGroupService.queryOcUserGroupByUserId(user.getId());
            user.setUserGroups(BeanCopierUtils.copyListProperties(userGroupList, UserGroupVO.UserGroup.class));
            // 装饰 服务器组
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(user.getId());
            user.setServerGroups(convert(user, serverGroupList));
            // 装饰 ApiToken
            List<OcUserApiToken> userApiTokens = ocUserApiTokenService.queryOcUserApiTokenByUsername(user.getUsername());
            List<UserApiTokenVO.UserApiToken> apiTokens = BeanCopierUtils.copyListProperties(userApiTokens, UserApiTokenVO.UserApiToken.class).stream().map(e -> {
                e.setToken("申请后不可查看");
                return e;
            }).collect(Collectors.toList());
            user.setApiTokens(apiTokens);
            // 装饰 凭据
            List<OcUserCredential> credentials = ocUserCredentialService.queryOcUserCredentialByUserId(user.getId());
            Map<String, UserCredentialVO.UserCredential> credentialMap = Maps.newHashMap();
            for (OcUserCredential credential : credentials)
                credentialMap.put(CredentialType.getName(credential.getCredentialType()), BeanCopierUtils.copyProperties(credential, UserCredentialVO.UserCredential.class));
            user.setCredentialMap(credentialMap);
            // 用户属性
            Map<String, Object> attributeMap = Maps.newHashMap();
            attributeMap.put("jumpserverPubkey", jumpserverCenter.checkUserPubkeyExist(user.getUsername()));
            OcAccount gitlabAccount = ocAccountService.queryOcAccountByUsername(AccountType.GITLAB.getType(), user.getUsername());
            if (gitlabAccount != null)
                attributeMap.put("gitlabPubkey", gitlabAccount.getSshKey() == 1);
            user.setAttributeMap(attributeMap);
            // AliyunRAM
            user.setRamUsers(aliyunRAMFacade.queryRamUsersByUsername(user.getId()));
        }
        return user;
    }

    public UserVO.User decoratorFromLdapRepo(UserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            List<UserGroupVO.UserGroup> userGroups = Lists.newArrayList();
            personRepo.searchUserGroupByUsername(user.getUsername()).forEach(e -> {
                OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(e);
                if (ocUserGroup != null)
                    userGroups.add(BeanCopierUtils.copyProperties(ocUserGroup, UserGroupVO.UserGroup.class));
            });
            user.setUserGroups(userGroups);
        }
        return user;
    }

    private List<ServerGroupVO.ServerGroup> convert(UserVO.User user, List<OcServerGroup> serverGroupList) {
        return serverGroupList.stream().map(e -> {
            ServerGroupVO.ServerGroup serverGroup = BeanCopierUtils.copyProperties(e, ServerGroupVO.ServerGroup.class);
            OcUserPermission permission = new OcUserPermission();
            permission.setBusinessType(BusinessType.SERVER_ADMINISTRATOR_ACCOUNT.getType());
            permission.setBusinessId(e.getId());
            permission.setUserId(user.getId());
            permission = ocUserPermissionService.queryOcUserPermissionByUniqueKey(permission);
            serverGroup.setIsAdmin(permission != null);
            return serverGroup;
        }).collect(Collectors.toList());
    }
}
