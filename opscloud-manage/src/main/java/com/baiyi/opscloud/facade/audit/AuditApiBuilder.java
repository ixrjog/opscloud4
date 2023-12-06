package com.baiyi.opscloud.facade.audit;

import com.baiyi.opscloud.domain.generator.opscloud.AuditApi;
import com.baiyi.opscloud.service.audit.AuditApiService;
import lombok.Builder;
import lombok.Data;

/**
 * @Author baiyi
 * @Date 2023/12/6 10:38
 * @Version 1.0
 */
public class AuditApiBuilder {

    @Data
    @Builder
    public static class DefaultAuditApi {

        private String api;
        private String username;
        private Boolean useAk;

        private AuditApiService auditApiService;

        public void save() {
            AuditApi auditApi = AuditApi.builder()
                    .api(this.api)
                    .username(this.username)
                    .useAk(this.useAk)
                    .build();
            auditApiService.add(auditApi);
        }

    }

    public DefaultAuditApi build() {
        return standing;
    }

    private final DefaultAuditApi standing = DefaultAuditApi.builder().build();

    private AuditApiBuilder() {
    }

    public static AuditApiBuilder builder() {
        return new AuditApiBuilder();
    }

    public AuditApiBuilder api(String api) {
        standing.setApi(api);
        return this;
    }

    public AuditApiBuilder username(String username) {
        standing.setUsername(username);
        return this;
    }

    public AuditApiBuilder useAk(boolean useAk) {
        standing.setUseAk(useAk);
        return this;
    }

    public AuditApiBuilder auditApiService(AuditApiService auditApiService) {
        standing.setAuditApiService(auditApiService);
        return this;
    }

}
