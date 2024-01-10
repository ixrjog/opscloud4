package com.baiyi.opscloud.facade.audit;

/**
 * @Author baiyi
 * @Date 2023/12/6 10:28
 * @Version 1.0
 */
public interface OperationalAuditFacade {

    void save(String api, Boolean useAk);

}
