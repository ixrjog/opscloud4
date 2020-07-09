package com.baiyi.opscloud.decorator.user;

import com.baiyi.opscloud.common.util.BeanCopierUtils;
import com.baiyi.opscloud.domain.generator.jumpserver.UsersUsergroup;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverUsersUserVO;
import com.baiyi.opscloud.domain.vo.jumpserver.JumpserverUsersUsergroupVO;
import com.baiyi.opscloud.service.jumpserver.UsersUsergroupService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2020/3/13 9:59 上午
 * @Version 1.0
 */
@Component
public class UsersUserDecorator {

    @Resource
    private UsersUsergroupService usersUsergroupService;

    public JumpserverUsersUserVO.UsersUser decorator(JumpserverUsersUserVO.UsersUser usersUser, Integer extend) {
        if (extend != null && extend == 1) {
            // 装饰 用户组
            List<UsersUsergroup> usersUsergroupList = usersUsergroupService.queryUsersUsergroupByUsername(usersUser.getUsername());
            usersUser.setUsersUsergroups(BeanCopierUtils.copyListProperties(usersUsergroupList, JumpserverUsersUsergroupVO.UsersUsergroup.class));
        }
        return usersUser;
    }
}
