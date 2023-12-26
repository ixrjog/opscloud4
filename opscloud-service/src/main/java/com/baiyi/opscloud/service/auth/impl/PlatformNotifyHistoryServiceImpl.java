package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.PlatformNotifyHistory;
import com.baiyi.opscloud.mapper.PlatformNotifyHistoryMapper;
import com.baiyi.opscloud.service.auth.PlatformNotifyHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author 修远
 * @Date 2022/9/8 6:18 PM
 * @Since 1.0
 */

@Service
@RequiredArgsConstructor
public class PlatformNotifyHistoryServiceImpl implements PlatformNotifyHistoryService {

    private final PlatformNotifyHistoryMapper platformNotifyHistoryMapper;

    @Override
    public void add(PlatformNotifyHistory platformNotifyHistory) {
        platformNotifyHistoryMapper.insert(platformNotifyHistory);
    }

}