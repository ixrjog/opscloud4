package com.baiyi.opscloud.application;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import com.google.common.base.Joiner;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;


/**
 * @Author baiyi
 * @Date 2023/10/23 13:45
 * @Version 1.0
 */
public class ApplicationTest extends BaseUnit {

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserService userService;

    @Resource
    private UserPermissionService userPermissionService;

    // A1=46 A2=47 A3=48 B1=49 B2=50 B3=51
    private final static int TAG_ID = 51;

    @Test
    void test() {
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .page(1)
                .length(200)
                .tagId(TAG_ID)
                .build();

        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);

        for (Application datum : table.getData()) {
            UserPermission userPermission = UserPermission.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .businessId(datum.getId())
                    .build();
            List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission)
                    .stream()
                    .filter(e -> "admin".equalsIgnoreCase(e.getPermissionRole()))
                    .toList();
            print(datum.getName() + "|" + toUsersDisplayName(userPermissions));
        }
    }

    private String toUsersDisplayName(List<UserPermission> userPermissions) {
        if (CollectionUtils.isEmpty(userPermissions)) {
            return "-";
        }
        return Joiner.on(",").join(userPermissions.stream().map(e -> userService.getById(e.getUserId()).getDisplayName()).collect(Collectors.toList()));
    }

}
