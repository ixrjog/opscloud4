package com.baiyi.opscloud.test;

import com.alibaba.fastjson.JSON;
import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.domain.types.BusinessTypeEnum;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author baiyi
 * @Date 2021/9/13 10:47 上午
 * @Version 1.0
 */
public class ApplicationTest extends BaseUnit {

    @Resource
    private UserPermissionService userPermissionService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private ApplicationResourceService applicationResourceService;

    @Test
    void initApplicationUserPermission() {
        ApplicationParam.ApplicationPageQuery pageQuery = ApplicationParam.ApplicationPageQuery.builder()
                .build();
        pageQuery.setPage(1);
        pageQuery.setLength(1000);
        DataTable<Application> table = applicationService.queryPageByParam(pageQuery);
        table.getData().forEach(e -> {
            List<ApplicationResource> resources = applicationResourceService.queryByApplication(e.getId(), BusinessTypeEnum.SERVERGROUP.name());
            if (CollectionUtils.isEmpty(resources)) return;
            resources.forEach(g -> {
                int serverGroupId = g.getBusinessId();
                // 查询服务器组授权的所有用户
                UserPermission userPermission = UserPermission.builder()
                        .businessId(serverGroupId)
                        .businessType(BusinessTypeEnum.SERVERGROUP.getType())
                        .build();
                List<UserPermission> userPermissions = userPermissionService.queryByBusiness(userPermission);
                // 给应用添加相同的用户授权
                if (!CollectionUtils.isEmpty(userPermissions)) {
                    userPermissions.forEach(p -> {
                        UserPermission permission = UserPermission.builder()
                                .businessId(g.getApplicationId())
                                .businessType(BusinessTypeEnum.APPLICATION.getType())
                                .userId(p.getUserId())
                                .build();
                        System.out.println(JSON.toJSONString(permission));
                        try{
                            userPermissionService.add(permission);
                        }catch (Exception ignored){
                        }
                    });
                }
            });
        });


    }
}
