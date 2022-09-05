package com.baiyi.opscloud.facade.application;

import com.baiyi.opscloud.domain.generator.opscloud.User;

import java.util.List;

/**
 * @Author baiyi
 * @Date 2022/9/5 15:17
 * @Version 1.0
 */
public interface ApplicationAlertFacade {

    List<User> queryByApplicationNames(List<String> names);

    void refreshCache(String name);

}
