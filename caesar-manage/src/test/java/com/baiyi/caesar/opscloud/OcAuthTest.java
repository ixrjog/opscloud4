package com.baiyi.caesar.opscloud;

import com.baiyi.caesar.BaseUnit;
import com.baiyi.caesar.domain.DataTable;
import com.baiyi.caesar.domain.generator.caesar.AuthUserRole;
import com.baiyi.caesar.domain.vo.auth.UserRoleVO;
import com.baiyi.caesar.opscloud.provider.OcAuthProvider;
import com.baiyi.caesar.service.auth.AuthUserRoleService;
import com.baiyi.caesar.service.user.UserService;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @Author <a href="mailto:xiuyuan@xinc818.group">修远</a>
 * @Date 2021/6/29 10:20 上午
 * @Since 1.0
 */
public class OcAuthTest extends BaseUnit {

    @Resource
    private OcAuthProvider ocAuthProvider;

    @Resource
    private AuthUserRoleService authUserRoleService;

    @Resource
    private UserService userService;

    @Test
    void syncUserRole() {
        try {
            DataTable<UserRoleVO.UserRole> table = ocAuthProvider.queryUserRolePage(5);
            table.getData().forEach(userRole -> {
                if (userService.getByUsername(userRole.getUsername()) != null) {
                    AuthUserRole authUserRole = new AuthUserRole();
                    authUserRole.setRoleId(userRole.getRoleId());
                    authUserRole.setUsername(userRole.getUsername());
                    if (authUserRoleService.queryByUniqueKey(authUserRole) == null) {
                        authUserRoleService.add(authUserRole);
                    }
                }
            });
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }
}
