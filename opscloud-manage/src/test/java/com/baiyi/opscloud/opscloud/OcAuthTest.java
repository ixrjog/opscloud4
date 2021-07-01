package com.baiyi.opscloud.opscloud;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.AuthUserRole;
import com.baiyi.opscloud.domain.vo.auth.UserRoleVO;
import com.baiyi.opscloud.opscloud.provider.OcAuthProvider;
import com.baiyi.opscloud.service.auth.AuthUserRoleService;
import com.baiyi.opscloud.service.user.UserService;
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
