package com.baiyi.opscloud.service.audit.impl;

import com.baiyi.opscloud.domain.generator.opscloud.AuditApi;
import com.baiyi.opscloud.mapper.AuditApiMapper;
import com.baiyi.opscloud.service.audit.AuditApiService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @Author baiyi
 * @Date 2023/12/6 10:17
 * @Version 1.0
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@AllArgsConstructor
public class AuditApiServiceImpl implements AuditApiService {

    private final AuditApiMapper auditApiMapper;

    @Override
    public void add(AuditApi auditApi) {
        auditApiMapper.insert(auditApi);
    }

}