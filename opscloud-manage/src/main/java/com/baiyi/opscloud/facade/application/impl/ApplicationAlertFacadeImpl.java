package com.baiyi.opscloud.facade.application.impl;

import com.baiyi.opscloud.domain.generator.opscloud.User;
import com.baiyi.opscloud.facade.application.ApplicationAlertFacade;
import com.google.common.collect.Maps;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author baiyi
 * @Date 2022/9/5 15:17
 * @Version 1.0
 */
@Component
@RequiredArgsConstructor
public class ApplicationAlertFacadeImpl implements ApplicationAlertFacade {

    private final ApplicationAlertHelper applicationAlertHelper;

    /**
     * 按应用名查询用户列表
     *
     * @param names
     * @return
     */
    @Override
    public List<User> queryByApplicationNames(List<String> names) {
        if (CollectionUtils.isEmpty(names)) {
            return Collections.emptyList();
        }
        Map<String, User> userMap = Maps.newHashMap();
        for (String name : names) {
            List<User> users = applicationAlertHelper.queryByApplicationName(name);
            if (CollectionUtils.isEmpty(users)) {
                continue;
            }
            for (User user : users) {
                if (!userMap.containsKey(user.getUsername())) {
                    userMap.put(user.getUsername(), user);
                }
            }
        }
        if (userMap.isEmpty()) {
            return Collections.emptyList();
        }
        return userMap.keySet().stream().map(userMap::get).collect(Collectors.toList());
    }

    /**
     * 刷新应用授权用户
     * @param name
     */
    @Override
    @Async
    public void refreshCache(String name) {
        applicationAlertHelper.evictWithApplicationName(name);
        applicationAlertHelper.queryByApplicationName(name);
    }

}
