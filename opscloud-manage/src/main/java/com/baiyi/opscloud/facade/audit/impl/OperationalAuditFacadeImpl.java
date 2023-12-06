package com.baiyi.opscloud.facade.audit.impl;

import com.baiyi.opscloud.common.holder.SessionHolder;
import com.baiyi.opscloud.facade.audit.AuditApiBuilder;
import com.baiyi.opscloud.facade.audit.OperationalAuditFacade;
import com.baiyi.opscloud.service.audit.AuditApiService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author baiyi
 * @Date 2023/12/6 10:28
 * @Version 1.0
 */
@Slf4j
@Component
@AllArgsConstructor
public class OperationalAuditFacadeImpl implements OperationalAuditFacade {

    private final AuditApiService auditApiService;

    /**
     * 只审计有登录信息的API接口
     * @param api
     * @param useAk
     */
    public void save(String api, Boolean useAk) {
        if (SessionHolder.isEmpty()) {
            return;
        }
        AuditApiBuilder.builder()
                .api(api)
                .username(SessionHolder.getUsername())
                .useAk(useAk)
                .auditApiService(auditApiService)
                .build()
                .save();
    }

}
