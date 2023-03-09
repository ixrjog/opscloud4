package com.baiyi.opscloud.packer.auth;

import com.baiyi.opscloud.common.util.BeanCopierUtil;
import com.baiyi.opscloud.domain.generator.opscloud.AuthUserRole;
import com.baiyi.opscloud.domain.vo.auth.AuthRoleVO;
import com.baiyi.opscloud.service.auth.AuthRoleService;
import com.baiyi.opscloud.service.auth.AuthUserRoleService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2021/5/12 9:39 上午
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class AuthRolePacker {

    private final AuthUserRoleService authUserRoleService;

    private final AuthRoleService authRoleService;

    public void wrap(AuthRoleVO.IRoles iRoles) {
        if (StringUtils.isEmpty(iRoles.getUsername())) {
            return;
        }
        List<AuthUserRole> roles = authUserRoleService.queryByUsername(iRoles.getUsername());
        iRoles.setRoles(roles.stream().map(e ->
                BeanCopierUtil.copyProperties(authRoleService.getById(e.getRoleId()), AuthRoleVO.Role.class)
        ).collect(Collectors.toList()));
    }

}
