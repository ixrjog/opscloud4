package com.baiyi.opscloud.decorator.user;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.user.UserGroupVO;
import com.baiyi.opscloud.domain.vo.user.UserVO;
import com.baiyi.opscloud.ldap.repo.GroupRepo;
import com.baiyi.opscloud.service.user.OcUserService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/2/24 8:04 下午
 * @Version 1.0
 */
@Component("UserGroupDecorator")
public class UserGroupDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private GroupRepo groupRepo;

    // from mysql
    public UserGroupVO.UserGroup decorator(UserGroupVO.UserGroup userGroup, Integer extend) {
        if (extend != null && extend == 1) {
            List<OcUser> userList = ocUserService.queryOcUserByUserGroupId(userGroup.getId());
            userGroup.setUsers(BeanCopierUtils.copyListProperties(userList, UserVO.User.class));
        }
        return userGroup;
    }

    public UserGroupVO.UserGroup decoratorFromLdapRepo(UserGroupVO.UserGroup userGroup, Integer extend) {
        if (extend != null && extend == 1) {
            List<String> usernameList = groupRepo.queryGroupMember(userGroup.getName());
            //Map<String, OcUserVO.User> userMap = Maps.newHashMap();
            List<UserVO.User> users = Lists.newArrayList();
            for (String username : usernameList) {
                OcUser ocUser = ocUserService.queryOcUserByUsername(username);
                if (ocUser != null)
                    users.add(BeanCopierUtils.copyProperties(ocUser, UserVO.User.class));
            }
            userGroup.setUsers(users);
        }
        return userGroup;
    }
}
