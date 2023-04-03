package com.baiyi.opscloud.datasource.aliyun;

import com.aliyuncs.cms.model.v20190101.DescribeContactListResponse;
import com.baiyi.opscloud.common.datasource.AliyunConfig;
import com.baiyi.opscloud.datasource.aliyun.base.BaseAliyunTest;
import com.baiyi.opscloud.datasource.aliyun.cms.driver.AliyunCmsContactDriver;
import com.baiyi.opscloud.datasource.aliyun.cms.driver.AliyunCmsContactGroupDriver;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.springframework.util.CollectionUtils;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/7/13 14:16
 * @Version 1.0
 */
public class AliyunCmsTest extends BaseAliyunTest {

    @Resource
    private AliyunCmsContactDriver aliyunCmsContactDriver;

    @Resource
    private AliyunCmsContactGroupDriver aliyunCmsContactGroupDriver;

    @Resource
    private UserService userService;

    @Resource
    private ApplicationService applicationService;

    @Resource
    private UserPermissionService userPermissionService;

    @Test
    void listContactsTest() {
        List<DescribeContactListResponse.Contact> contacts = aliyunCmsContactDriver.listContacts("cn-hangzhou", getConfig().getAliyun());
        print(contacts);
    }

    @Test
    void putContactTest() {
        List<User> users = userService.listActive();
        AliyunConfig.Aliyun aliyun = getConfig().getAliyun();
        users.forEach(u -> {
            List<UserPermission> permissions = userPermissionService.queryByUserPermission(u.getId(), BusinessTypeEnum.APPLICATION.getType());
            if (!CollectionUtils.isEmpty(permissions)) {
                boolean result = aliyunCmsContactDriver.putContact("cn-hangzhou", aliyun, u);
                print("username = " + u.getUsername() + ", result = " + result);
            }
        });
    }

    @Test
    void putContactGroupTest() {
        List<Application> applications = applicationService.queryAll();
        AliyunConfig.Aliyun aliyun = getConfig().getAliyun();
        applications.forEach(a -> {
            UserPermission query = UserPermission.builder()
                    .businessType(BusinessTypeEnum.APPLICATION.getType())
                    .businessId(a.getId())
                    .build();
            List<UserPermission> permissions = userPermissionService.queryByBusiness(query)
                    .stream()
                    .filter(e -> "Admin".equalsIgnoreCase(e.getPermissionRole()))
                    .collect(Collectors.toList());
            List<String> contactNamess = Lists.newArrayList();
            if (!CollectionUtils.isEmpty(permissions)) {
                contactNamess = permissions.stream().map(p -> {
                    User user = userService.getById(p.getUserId());
                    return user.getDisplayName();
                }).collect(Collectors.toList());
            }
            if (!CollectionUtils.isEmpty(permissions)) {
                boolean result = aliyunCmsContactGroupDriver.putContactGroup("cn-hangzhou", aliyun, a, contactNamess);
                print("application = " + a.getName() + ", result = " + result);
            }
        });
    }

}
