package com.baiyi.caesar.packer.auth;

import com.baiyi.caesar.common.util.BeanCopierUtil;
import com.baiyi.caesar.domain.generator.caesar.AuthRole;
import com.baiyi.caesar.domain.generator.caesar.AuthUserRole;
import com.baiyi.caesar.vo.auth.AuthRoleVO;
import com.baiyi.caesar.vo.user.UserVO;
import com.baiyi.caesar.service.auth.AuthRoleService;
import com.baiyi.caesar.service.auth.AuthUserRoleService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/12 9:39 上午
 * @Version 1.0
 */
@Component
public class AuthRolePacker {

    @Resource
    private AuthUserRoleService authUserRoleService;

    @Resource
    private AuthRoleService authRoleService;

    public List<AuthRoleVO.Role> wrapVOList(List<AuthRole> data) {
        return BeanCopierUtil.copyListProperties(data, AuthRoleVO.Role.class);
    }

    public void wrap(UserVO.User user) {
        List<AuthUserRole> roles = authUserRoleService.queryByUsername(user.getUsername());
        user.setRoles(
                roles.stream().map(e ->
                        BeanCopierUtil.copyProperties(authRoleService.getById(e.getRoleId()), AuthRoleVO.Role.class)
                ).collect(Collectors.toList())
        );
    }

}
