package com.baiyi.opscloud.decorator;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.OcServerGroup;
import com.baiyi.opscloud.domain.generator.OcUserApiToken;
import com.baiyi.opscloud.domain.generator.OcUserGroup;
import com.baiyi.opscloud.domain.vo.server.OcServerGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserApiTokenVO;
import com.baiyi.opscloud.domain.vo.user.OcUserGroupVO;
import com.baiyi.opscloud.domain.vo.user.OcUserVO;
import com.baiyi.opscloud.ldap.repo.PersonRepo;
import com.baiyi.opscloud.service.server.OcServerGroupService;
import com.baiyi.opscloud.service.user.OcUserApiTokenService;
import com.baiyi.opscloud.service.user.OcUserGroupService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
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
    private PersonRepo personRepo;

    // from mysql
    public OcUserVO.User decorator(OcUserVO.User user, Integer extend) {
        user.setPassword("");
        if (extend != null && extend == 1) {
            // 装饰 用户组
            List<OcUserGroup> userGroupList = ocUserGroupService.queryOcUserGroupByUserId(user.getId());
            user.setUserGroups(BeanCopierUtils.copyListProperties(userGroupList, OcUserGroupVO.UserGroup.class));
            // 装饰 服务器组
            List<OcServerGroup> serverGroupList = ocServerGroupService.queryUerPermissionOcServerGroupByUserId(user.getId());
            user.setServerGroups(BeanCopierUtils.copyListProperties(serverGroupList, OcServerGroupVO.ServerGroup.class));
            // 装饰 ApiToken
            List<OcUserApiToken> userApiTokens = ocUserApiTokenService.queryOcUserApiTokenByUsername(user.getUsername());
            List<OcUserApiTokenVO.UserApiToken> apiTokens =  BeanCopierUtils.copyListProperties(userApiTokens, OcUserApiTokenVO.UserApiToken.class).stream().map(e -> {
                e.setToken("申请后不可查看");
                return e;
            }).collect(Collectors.toList());
            user.setApiTokens(apiTokens);
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
