package com.baiyi.opscloud.rbac;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.generator.opscloud.AuthRoleResource;
import com.baiyi.opscloud.service.auth.AuthRoleResourceService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * &#064;Author  baiyi
 * &#064;Date  2024/5/17 下午4:20
 * &#064;Version 1.0
 */
public class RbacTest extends BaseUnit {

    @Resource
    private AuthRoleResourceService authRoleResourceService;


    @Test
    void test() {
        // dev=4/ base=5
        List<AuthRoleResource> authRoleResourceList = authRoleResourceService.queryByRoleId(5);

        authRoleResourceList.forEach(e -> {
            AuthRoleResource authRoleResource = new AuthRoleResource();
            authRoleResource.setResourceId(e.getResourceId());
            authRoleResource.setRoleId(23);
            if (authRoleResourceService.getByUniqueKey(authRoleResource) == null) {
                authRoleResourceService.add(authRoleResource);
            }
        });


    }


}
