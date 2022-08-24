package com.baiyi.opscloud.service.auth.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuthPlatformLog;
import com.baiyi.opscloud.mapper.opscloud.AuthPlatformLogMapper;
import com.baiyi.opscloud.service.auth.AuthPlatformLogService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2022/8/22 14:47
 * @Version 1.0
 */
@Service
@RequiredArgsConstructor
public class AuthPlatformLogServiceImpl implements AuthPlatformLogService {

    private final AuthPlatformLogMapper authPlatformLogMapper;

    @Override
    public void add(AuthPlatformLog authPlatformLog) {
        authPlatformLogMapper.insert(authPlatformLog);
    }

}
