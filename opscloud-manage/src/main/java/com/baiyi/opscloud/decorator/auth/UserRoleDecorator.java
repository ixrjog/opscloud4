package com.baiyi.opscloud.decorator.auth;

import com.baiyi.opscloud.domain.generator.opscloud.OcAuthRole;
import com.baiyi.opscloud.domain.generator.opscloud.OcUser;
import com.baiyi.opscloud.domain.vo.auth.UserRoleVO;
import com.baiyi.opscloud.service.auth.OcAuthRoleService;
import com.baiyi.opscloud.service.user.OcUserService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author baiyi
 * @Date 2020/10/8 10:27 上午
 * @Version 1.0
 */
@Component("UserRoleDecorator")
public class UserRoleDecorator {

    @Resource
    private OcUserService ocUserService;

    @Resource
    private OcAuthRoleService ocAuthRoleService;

    /**
     * 插入用户角色详情
     *
     * @param userRole
     * @return
     */
    public UserRoleVO.UserRole decorator(UserRoleVO.UserRole userRole) {
        OcUser ocUser = ocUserService.queryOcUserByUsername(userRole.getUsername());
        userRole.setDisplayName(ocUser.getDisplayName());
        OcAuthRole ocAuthRole = ocAuthRoleService.queryOcAuthRoleById(userRole.getRoleId());
        userRole.setRoleName(ocAuthRole.getRoleName());
        userRole.setRoleComment(ocAuthRole.getComment());
        return userRole;
    }
}
