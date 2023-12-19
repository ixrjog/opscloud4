package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.common.configuration.CachingConfiguration;
import com.baiyi.opscloud.domain.constants.BusinessTypeEnum;
import com.baiyi.opscloud.domain.generator.opscloud.Application;
import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.domain.generator.opscloud.UserPermission;
import com.baiyi.opscloud.service.application.ApplicationService;
import com.baiyi.opscloud.service.user.UserPermissionService;
import com.baiyi.opscloud.service.user.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/9/5 15:36
 * @Version 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ApplicationAlertHelper {

    private final ApplicationService applicationService;

    private final UserPermissionService userPermissionService;

    private final UserService userService;

    @Cacheable(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'application_alert_name' + #name", unless = "#result == null")
    public List<User> queryByApplicationName(String name) {
        Application application = applicationService.getByName(name);
        if (application == null) {
            return Collections.emptyList();
        }
        UserPermission userPermission = UserPermission.builder()
                .businessId(application.getId())
                .businessType(BusinessTypeEnum.APPLICATION.getType())
                .build();
        return userPermissionService.queryByBusiness(userPermission).stream()
                .filter(e -> "Admin".equalsIgnoreCase(e.getPermissionRole()))
                .map(e -> userService.getById(e.getUserId()))
                .filter(User::getIsActive)
                .collect(Collectors.toList());
    }

    @CacheEvict(cacheNames = CachingConfiguration.Repositories.CACHE_FOR_1W, key = "'application_alert_name' + #name")
    public void evictWithApplicationName(String name) {
        log.info("清除应用告警查询用户数据缓存: application={}", name);
    }

}
