package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.base.CredentialType;
import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcServerGroup;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserApiToken;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserCredential;
import com.baiyi.opscloud.domain.generator.opscloud.OcUserGroup;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserCredentialVO;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.jumpserver.center.JumpserverCenter;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import com.baiyi.opscloud.service.user.OcUserCredentialService;
import com.baiyi.opscloud.service.user.OcUserGroupService;
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
    private PersonRepo personRepo;

    // from mysql
    public OcUserVO.User decorator(OcUserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            // 装饰 用户组
            List<OcUserGroup> userGroupList = ocUserGroupService.queryOcUserGroupByUserId(user.getId());
            user.setUserGroups(BeanCopierUtils.copyListProperties(userGroupList, OcUserGroupVO.UserGroup.class));
            // 装饰 服务器组
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryUserPermissionOcServerGroupByUserId(user.getId());
            user.setServerGroups(BeanCopierUtils.copyListProperties(serverGroupList, OcServerGroupVO.ServerGroup.class));
            // 装饰 ApiToken
            List<OcUserApiToken> userApiTokens = ocUserApiTokenService.queryOcUserApiTokenByUsername(user.getUsername());
            List<OcUserApiTokenVO.UserApiToken> apiTokens = BeanCopierUtils.copyListProperties(userApiTokens, OcUserApiTokenVO.UserApiToken.class).stream().map(e -> {
                e.setToken("申请后不可查看");
                return e;
            }).collect(Collectors.toList());
            user.setApiTokens(apiTokens);
            // 装饰 凭据
            List<OcUserCredential> credentials = ocUserCredentialService.queryOcUserCredentialByUserId(user.getId());
            Map<String, OcUserCredentialVO.UserCredential> credentialMap = Maps.newHashMap();
            for (OcUserCredential credential : credentials)
                credentialMap.put(CredentialType.getName(credential.getCredentialType()), BeanCopierUtils.copyProperties(credential, OcUserCredentialVO.UserCredential.class));
            user.setCredentialMap(credentialMap);
            // 用户属性
            Map<String, Object> attributeMap = Maps.newHashMap();
            attributeMap.put("jumpserverPubkey",jumpserverCenter.checkUserPubkeyExist(user.getUsername()));
            user.setAttributeMap(attributeMap);
        }
        return user;
    }

    public OcUserVO.User decoratorFromLdapRepo(OcUserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            List<String> groupNameList = personRepo.searchUserGroupByUsername(user.getUsername());
            List<OcUserGroupVO.UserGroup> userGroups = Lists.newArrayList();
            for (String groupName : groupNameList) {
                OcUserGroup ocUserGroup = ocUserGroupService.queryOcUserGroupByName(groupName);
                if (ocUserGroup != null)
                    userGroups.add(BeanCopierUtils.copyProperties(ocUserGroup, OcUserGroupVO.UserGroup.class));
            }
            user.setUserGroups(userGroups);
        }
        return user;
    }
}
