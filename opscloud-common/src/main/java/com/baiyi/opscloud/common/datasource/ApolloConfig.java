package com.baiyi.opscloud.common.datasource;

import com.baiyi.opscloud.common.datasource.base.BaseDsConfig;
import com.baiyi.opscloud.common.datasource.base.IFilterDsConfig;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Optional;

/**
 * @Author baiyi
 * @Date 2023/5/29 17:39
 * @Version 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApolloConfig extends BaseDsConfig {

    private Apollo apollo;

    @Data
    @NoArgsConstructor
    @Schema
    public static class Apollo implements IFilterDsConfig {
        private String version;
        @Schema(description = "门户")
        private Portal portal;

        private boolean filterInterceptorToken(String token) {
            return token.equals(Optional.of(this)
                    .map(Apollo::getPortal)
                    .map(Portal::getRelease)
                    .map(Release::getInterceptor)
                    .map(Interceptor::getToken)
                    .orElse(""));
        }

        @Override
        public boolean filter(String str) {
            return filterInterceptorToken(str);
        }
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Portal {
        private String url;
        private String token;
        private Release release;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Release {
        private Interceptor interceptor;
        private String notify;
    }

    @Data
    @NoArgsConstructor
    @Schema
    public static class Interceptor {
        private String token;
        @Schema(name = "需要拦截的命名空间列表")
        private List<String> namespaces;
        private List<String> envs;
        @Schema(name = "白名单")
        private List<String> whiteList;
    }

}