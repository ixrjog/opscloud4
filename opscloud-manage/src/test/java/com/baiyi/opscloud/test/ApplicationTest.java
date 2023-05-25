package com.baiyi.opscloud.test;

import com.baiyi.opscloud.BaseUnit;
import com.baiyi.opscloud.domain.DataTable;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.ApplicationResource;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.domain.param.application.ApplicationParam;
import com.baiyi.opscloud.service.application.ApplicationResourceService;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.google.common.base.Splitter;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

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
                .page(1)
                .length(1000)
                .build();
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
                        print(permission);
                        try {
                            userPermissionService.add(permission);
                        } catch (Exception ignored) {
                        }
                    });
                }
            });
        });
    }

    @Test
    public void test() {
        // 模拟一些JVM参数
        String javaOpts = "java -Dfile.encoding=UTF-8 -Xms4096M -Xmx4096M -Xmn2048M -XX:MetaspaceSize=128M -XX:MaxMetaspaceSize=256M -Xss256K -XX:+HeapDumpOnOutOfMemoryError -XX:+PrintGCDateStamps -XX:+DisableExplicitGC -XX:+UseConcMarkSweepGC -XX:+UseCMSInitiatingOccupancyOnly";

        // TODO 1 插入参数 -XX:CMSInitiatingOccupancyFraction=80 ；需判断是否存在

        // TODO 2 修改参数 -Xmx4096M 为 -Xmx8096M

        List<String> opts = Splitter.on(" ").splitToList(javaOpts);

        StringBuilder sb = new StringBuilder();
        for (String opt : opts) {
            System.out.println(opt);
            // 匹配前缀
            if(opt.startsWith("-Xmx")){
                opt = "-Xmx8096M";
            }
            // 首次append会在头部添加一个多余的空格
            sb.append(" ").append(opt);
        }
        // trim() 去除首尾空格
        System.out.println(sb.toString().trim());
    }

}
