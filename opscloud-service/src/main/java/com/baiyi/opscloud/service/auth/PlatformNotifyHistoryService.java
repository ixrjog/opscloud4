package com.baiyi.opscloud.service.auth;

import com.baiyi.opscloud.domain.generator.opscloud.PlatformNotifyHistory;

/**
 * @Author 修远
 * @Date 2022/9/8 6:17 PM
 * @Since 1.0
 */
public interface PlatformNotifyHistoryService {

    void add(PlatformNotifyHistory platformNotifyHistory);

}